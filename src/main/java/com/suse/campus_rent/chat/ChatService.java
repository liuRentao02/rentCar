package com.suse.campus_rent.chat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.core.WebSocketService;
import com.suse.campus_rent.dto.common.MessageAdd;
import com.suse.campus_rent.entity.Conversation;
import com.suse.campus_rent.entity.Message;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.ConversationMapper;
import com.suse.campus_rent.mapper.MessageMapper;
import com.suse.campus_rent.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final WebSocketService webSocketService;

    // ========== 用户端接口 ==========

    /**
     * 获取或创建用户与管理员的未关闭会话（用户打开组件时调用）
     */
    @Transactional
    public Long getOrCreateUserConversation(Long userId) {
        // 1. 查询该用户未关闭的会话
        LambdaQueryWrapper<Conversation> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("JSON_CONTAINS(user_id, CAST({0} AS JSON))", userId);
        Conversation existing = conversationMapper.selectOne(wrapper);
        if (existing != null) {
            return existing.getId();
        }

        // 2. 分配一个管理员
        List<User> admins = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .eq(User::getRole, "admin"));
        if (admins.isEmpty()) {
            throw new BusinessException("暂无管理员，无法发起聊天");
        }
        Long adminId = admins.get(0).getId();

        // 3. 创建新会话
        Conversation con = new Conversation();
        con.setTime(LocalDateTime.now());
        con.setUserId(List.of(userId, adminId));
        conversationMapper.insert(con);
        return con.getId();
    }

    /**
     * 用户发送消息（会话ID可选）
     */
    @Transactional
    public Long userSendMessage(MessageAdd add) {
        if (add.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        Long conversationId = add.getConversationId();
        if (conversationId == null) {
            conversationId = getOrCreateUserConversation(add.getId());
        } else {
            checkUserInConversation(conversationId, add.getId());
        }
        sendMessageInternal(conversationId, add.getId(), add.getType(), add.getContent());

        return conversationId;
    }

    /**
     * 获取用户自己的会话列表（未关闭的）
     */
    public List<Conversation> getUserConversations(Long userId) {
        LambdaQueryWrapper<Conversation> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("JSON_EXTRACT(user_id, '$') LIKE CONCAT('%', {0}, '%')", userId);
        return conversationMapper.selectList(wrapper);
    }

    // ========== 管理员端接口 ==========

    /**
     * 管理员获取所有未关闭的会话
     */
    public List<Conversation> getAllActiveConversations() {
        return conversationMapper.selectList(null);
    }

    /**
     * 管理员向指定会话发送消息
     */
    @Transactional
    public void adminSendMessage(Long conversationId, Long adminId, Integer type, String content) {
        checkUserInConversation(conversationId, adminId);
        // 可选：额外校验 adminId 对应的用户角色是否为管理员
        sendMessageInternal(conversationId, adminId, type, content);
    }

    // ========== 通用接口 ==========

    /**
     * 获取会话消息（带权限校验）
     */
    public List<Message> getMessagesByConversationId(Long conversationId, Long userId) {
        checkUserInConversation(conversationId, userId);
        return messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getConversationId, conversationId)
                        .orderByAsc(Message::getTime)
        );
    }

    /**
     * 删除消息（带权限校验）
     */
    @Transactional
    public void deleteMessage(Long conversationId, Long messageId, Long operatorId) {
        checkUserInConversation(conversationId, operatorId);
        // 可选：进一步校验 operatorId 是否是消息的发送者
        messageMapper.delete(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getConversationId, conversationId)
                        .eq(Message::getId, messageId)
        );
    }

    /**
     * 关闭会话（软删除）
     */
    @Transactional
    public void closeConversation(Long conversationId, Long operatorId) {
        checkUserInConversation(conversationId, operatorId);
        Conversation conv = conversationMapper.selectById(conversationId);
        conversationMapper.deleteById(conv);

        messageMapper.delete(new LambdaQueryWrapper<Message>().eq(Message::getConversationId, conversationId));

        // 获取对方用户ID并推送关闭事件
        Long otherUserId = getOtherParticipant(conv, operatorId);
        webSocketService.sendToUser(otherUserId, "/queue/conversation-closed", conversationId);
    }

    // ========== 私有辅助方法 ==========

    private void sendMessageInternal(Long conversationId, Long senderId, Integer type, String content) {
        // 1. 保存消息
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setUserId(senderId);
        message.setType(type);
        message.setText(content);
        message.setTime(LocalDateTime.now());
        messageMapper.insert(message);

        // 2. 获取接收者ID
        Conversation conv = conversationMapper.selectById(conversationId);
        Long receiverId = getOtherParticipant(conv, senderId);

        // 3. 通过 WebSocket 推送消息
        webSocketService.sendToUser(receiverId, "/queue/messages", message);
    }

    private void checkUserInConversation(Long conversationId, Long userId) {
        Conversation conv = conversationMapper.selectById(conversationId);
        if (conv == null) {
            throw new BusinessException("会话不存在或已关闭");
        }
        if (!conv.getUserId().contains(userId)) {
            throw new BusinessException("您不是该会话的参与者");
        }
    }

    private Long getOtherParticipant(Conversation conv, Long currentUserId) {
        List<Long> userIds = conv.getUserId();
        if (userIds.size() != 2) {
            throw new BusinessException("会话参与者数量异常");
        }
        return userIds.get(0).equals(currentUserId) ? userIds.get(1) : userIds.get(0);
    }

    public Long createOrGetConversationWithUser(Long adminId, Long userId) {
        // 1. 校验 admin 角色
        User admin = userMapper.selectById(adminId);
        if (admin == null || !"admin".equals(admin.getRole())) {
            throw new BusinessException("无权操作，需要管理员身份");
        }

        // 2. 查询是否已存在包含这两人的会话（未关闭，物理删除的会话不存在）
        LambdaQueryWrapper<Conversation> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("JSON_CONTAINS(user_id, CAST({0} AS JSON))", adminId)
                .apply("JSON_CONTAINS(user_id, CAST({0} AS JSON))", userId);
        Conversation existing = conversationMapper.selectOne(wrapper);
        if (existing != null) {
            return existing.getId();
        }

        // 3. 创建新会话
        Conversation conversation = new Conversation();
        conversation.setTime(LocalDateTime.now());
        conversation.setUserId(List.of(adminId, userId));
        conversationMapper.insert(conversation);
        return conversation.getId();
    }
}
package com.suse.campus_rent.chat;

import com.suse.campus_rent.service.app.service.impl.CustomUserDetails;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.AdminMessageSendDTO;
import com.suse.campus_rent.dto.common.MessageAdd;
import com.suse.campus_rent.entity.Conversation;
import com.suse.campus_rent.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // ========== 用户端接口 ==========

    @GetMapping("/user/conversations")
    public Result<List<Conversation>> getUserConversations() {
        Long currentUserId = getCurrentUserId();
        List<Conversation> conversations = chatService.getUserConversations(currentUserId);
        return Result.success(conversations);
    }

    @PostMapping("/user/send")
    public Result<Long> userSendMessage(@RequestBody MessageAdd add) {
        Long currentUserId = getCurrentUserId();
        add.setId(currentUserId);
        Long id = chatService.userSendMessage(add);
        return Result.success(id);
    }

    // ========== 管理员端接口 ==========

    @GetMapping("/admin/conversations")
    public Result<List<Conversation>> getAllActiveConversations() {
        List<Conversation> conversations = chatService.getAllActiveConversations();
        return Result.success(conversations);
    }

    @PostMapping("/admin/send")
    public Result<?> adminSendMessage(@RequestBody AdminMessageSendDTO dto) {
        Long currentAdminId = getCurrentUserId();
        chatService.adminSendMessage(dto.getConversationId(), currentAdminId, dto.getType(), dto.getContent());
        return Result.success();
    }

    // 在 ChatController 中添加（管理员端接口区域）

    @PostMapping("/admin/conversation/with-user/{userId}")
    public Result<Long> createOrGetConversationWithUser(@PathVariable Long userId) {
        Long currentAdminId = getCurrentUserId();
        Long conversationId = chatService.createOrGetConversationWithUser(currentAdminId, userId);
        return Result.success(conversationId);
    }

    // ========== 通用接口 ==========

    @GetMapping("/messages/{conversationId}")
    public Result<List<Message>> getMessages(@PathVariable Long conversationId) {
        Long currentUserId = getCurrentUserId();
        List<Message> messages = chatService.getMessagesByConversationId(conversationId, currentUserId);
        return Result.success(messages);
    }

    @DeleteMapping("/message/{conversationId}/{messageId}")
    public Result<?> deleteMessage(@PathVariable Long conversationId,
                                   @PathVariable Long messageId) {
        Long currentUserId = getCurrentUserId();
        chatService.deleteMessage(conversationId, messageId, currentUserId);
        return Result.success();
    }

    @DeleteMapping("/conversation/{conversationId}/close")
    public Result<?> closeConversation(@PathVariable Long conversationId) {
        Long currentUserId = getCurrentUserId();
        chatService.closeConversation(conversationId, currentUserId);
        return Result.success();
    }

    // ========== 辅助方法 ==========
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("未登录");
        }
        Object principal = authentication.getPrincipal();
        // 适配 CustomUserDetails
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser().getId();
        }
        // 兼容其他类型
        if (principal instanceof Long) {
            return (Long) principal;
        }
        if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                throw new RuntimeException("无法解析用户ID字符串");
            }
        }
        if (principal instanceof Number) {
            return ((Number) principal).longValue();
        }
        throw new RuntimeException("无法获取用户ID，principal类型：" + principal.getClass());
    }
}
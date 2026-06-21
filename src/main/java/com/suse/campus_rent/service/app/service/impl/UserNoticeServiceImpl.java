package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.app.AppNoticeQueryDTO;
import com.suse.campus_rent.entity.Notice;
import com.suse.campus_rent.entity.NoticeAttachment;
import com.suse.campus_rent.entity.UserNoticeRead;
import com.suse.campus_rent.mapper.NoticeAttachmentMapper;
import com.suse.campus_rent.mapper.NoticeMapper;
import com.suse.campus_rent.mapper.UserNoticeReadMapper;
import com.suse.campus_rent.service.app.service.UserNoticeService;
import com.suse.campus_rent.vo.app.NoticeDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserNoticeServiceImpl implements UserNoticeService {

    private final NoticeMapper noticeMapper;
    private final NoticeAttachmentMapper attachmentMapper;
    private final UserNoticeReadMapper userNoticeReadMapper;

    @Override
    public IPage<Notice> listPublishedNotices(AppNoticeQueryDTO queryDTO) {
        Page<Notice> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return noticeMapper.selectPublishedNotices(page, queryDTO);
    }

    @Override
    @Transactional
    public NoticeDetailVO getNoticeDetail(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice == null || !"published".equals(notice.getStatus())) {
            throw new BusinessException("公告不存在或未发布");
        }

        // 增加浏览量
        noticeMapper.incrementViewCount(id);

        NoticeDetailVO detailVO = new NoticeDetailVO();
        BeanUtils.copyProperties(notice, detailVO);
        // 查询附件
        List<NoticeAttachment> attachments = attachmentMapper.selectByNoticeId(id);
        detailVO.setNoticeAttachments(attachments);
        return detailVO;
    }

    @Override
    public String putReadNotice(Long id, Long userId) {

        if (existsRead(userId, id)) {
            return "fail";
        }

        UserNoticeRead userNoticeRead = new UserNoticeRead();
        userNoticeRead.setNoticeId(id);
        userNoticeRead.setUserId(userId);
        userNoticeRead.setReadTime(LocalDateTime.now());
        userNoticeRead.setCreateTime(LocalDateTime.now());
        int insert = userNoticeReadMapper.insert(userNoticeRead);
        return insert == 1 ? "success" : "fail";
    }

    @Override
    public String deleteNotice(Long noticeId, Long userId) {
        // 1. 先查询该用户对该公告的操作记录
        LambdaQueryWrapper<UserNoticeRead> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNoticeRead::getUserId, userId)
                .eq(UserNoticeRead::getNoticeId, noticeId);
        UserNoticeRead record = userNoticeReadMapper.selectOne(queryWrapper);

        LocalDateTime now = LocalDateTime.now();

        if (record != null) {
            // 2. 如果记录存在（说明之前读过）
            if (record.getDeleteTime() != null) {
                // 如果已经有删除时间，说明已经删过了，直接返回
                return "success";
            }
            // 更新删除时间
            LambdaUpdateWrapper<UserNoticeRead> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserNoticeRead::getId, record.getId())
                    .set(UserNoticeRead::getDeleteTime, now);
            userNoticeReadMapper.update(null, updateWrapper);
        } else {
            // 3. 如果记录不存在（说明之前未读，直接删除）
            // 直接插入一条新记录，同时设置已读时间和删除时间
            UserNoticeRead newRecord = new UserNoticeRead();
            newRecord.setUserId(userId);
            newRecord.setNoticeId(noticeId);
            newRecord.setReadTime(now);       // 标记为已读
            newRecord.setDeleteTime(now);     // 标记为已删除
            newRecord.setCreateTime(now);
            userNoticeReadMapper.insert(newRecord);
        }
        return "success";
    }


    /**
     * 检查用户是否已读某公告
     */
    private boolean existsRead(Long userId, Long noticeId) {
        LambdaQueryWrapper<UserNoticeRead> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserNoticeRead::getUserId, userId)
                .eq(UserNoticeRead::getNoticeId, noticeId);
        return userNoticeReadMapper.exists(wrapper);
    }
}
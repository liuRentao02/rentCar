package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.NoticeCreateDTO;
import com.suse.campus_rent.dto.admin.NoticeQueryDTO;
import com.suse.campus_rent.dto.admin.NoticeUpdateDTO;
import com.suse.campus_rent.entity.Notice;
import com.suse.campus_rent.entity.NoticeAttachment;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.entity.UserNoticeRead;
import com.suse.campus_rent.mapper.NoticeAttachmentMapper;
import com.suse.campus_rent.mapper.NoticeMapper;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.mapper.UserNoticeReadMapper;
import com.suse.campus_rent.service.admin.service.NoticeService;
import com.suse.campus_rent.util.FileUploadUtil;
import com.suse.campus_rent.vo.admin.AttachmentVO;
import com.suse.campus_rent.vo.admin.NoticeDetailVO;
import com.suse.campus_rent.vo.admin.NoticeListVO;
import com.suse.campus_rent.vo.admin.NoticeStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("adminNoticeService")
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private final NoticeAttachmentMapper noticeAttachmentMapper;
    private final FileUploadUtil fileUploadUtil;
    private final UserMapper userMapper;
    private final UserNoticeReadMapper userNoticeReadMapper;

    @Override
    public NoticeStatisticsVO getStatistics() {
        return noticeMapper.selectStatistics();
    }

    @Override
    public IPage<NoticeListVO> listNotices(NoticeQueryDTO queryDTO) {
        Page<NoticeListVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return noticeMapper.selectNoticeList(page, queryDTO);
    }

    @Override
    public NoticeDetailVO getNoticeDetail(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }

        NoticeDetailVO detailVO = new NoticeDetailVO();
        BeanUtils.copyProperties(notice, detailVO);
        detailVO.setPublisher(notice.getPublisherName());
        // 查询附件
        List<NoticeAttachment> attachments = noticeAttachmentMapper.selectByNoticeId(id);
        detailVO.setAttachments(attachments);
        return detailVO;
    }

    @Override
    @Transactional
    public void createNotice(NoticeCreateDTO createDTO) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(createDTO, notice);
        // 默认值处理
        if (!StringUtils.hasText(createDTO.getStatus())) {
            notice.setStatus("draft");
        }
        if (!StringUtils.hasText(createDTO.getPriority())) {
            notice.setPriority("normal");
        }

        if (createDTO.getUserId() != -1L) {
            LambdaQueryWrapper<User> uq = new LambdaQueryWrapper<>();
            uq.select(User::getId, User::getUsername);
            uq.eq(User::getId, createDTO.getUserId());

            User user = userMapper.selectOne(uq);
            if (user == null) {
                throw new BusinessException("查询失败，当前id没有用户");
            }
            notice.setPublisherId(user.getId());
            notice.setPublisherName(user.getUsername());
        } else {
            notice.setPublisherId(1L);
            notice.setPublisherName("系统");
        }

        // 处理定时发布：如果状态是已发布且设置了定时时间，则 publish_time 先不设置，由定时任务处理
        if ("published".equals(notice.getStatus()) && notice.getScheduledTime() != null) {
            // 定时发布：当前先保存为草稿，等定时任务发布
            notice.setStatus("draft");
            // 但保留 scheduledTime，定时任务会扫描并发布
        } else if ("published".equals(notice.getStatus())) {
            // 立即发布
            notice.setPublishTime(LocalDateTime.now());
        }
        notice.setViewCount(0);


        if (createDTO.getExpireTime() != null) {
            notice.setExpireTime(createDTO.getExpireTime());
        }

        noticeMapper.insert(notice);

        // 关联附件
        if (!CollectionUtils.isEmpty(createDTO.getAttachmentIds())) {
            for (Long attId : createDTO.getAttachmentIds()) {
                NoticeAttachment att = noticeAttachmentMapper.selectById(attId);
                if (att == null) {
                    throw new BusinessException("附件不存在: " + attId);
                }
                att.setNoticeId(notice.getId());
                noticeAttachmentMapper.updateById(att);
            }
        }
    }

    @Override
    @Transactional
    public void updateNotice(NoticeUpdateDTO updateDTO) {
        log.info("更新数据:{}", updateDTO);
        Notice notice = noticeMapper.selectById(updateDTO.getId());
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }

        // 1. 复制属性（这里对于 null 值不会覆盖）
        BeanUtils.copyProperties(updateDTO, notice);
        LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notice::getId, notice.getId());
        updateWrapper.set(Notice::getStatus, notice.getStatus());
        updateWrapper.set(Notice::getViewCount, notice.getViewCount());
        updateWrapper.set(Notice::getExpireTime, notice.getExpireTime());
        updateWrapper.set(Notice::getScheduledTime, notice.getScheduledTime());
        updateWrapper.set(Notice::getTitle, notice.getTitle());
        updateWrapper.set(Notice::getContent, notice.getContent());
        updateWrapper.set(Notice::getType, notice.getType());
        updateWrapper.set(Notice::getPriority, notice.getPriority());


        // 3. 状态逻辑
        if ("published".equals(notice.getStatus()) && notice.getScheduledTime() == null) {
            updateWrapper.set(Notice::getPublishTime, notice.getPublishTime());
        }
        if ("draft".equals(notice.getStatus()) && notice.getPublishTime() != null) {
            updateWrapper.set(Notice::getPublishTime, null);
        }

        // 4. 执行更新
        noticeMapper.update(updateWrapper);

        if (updateDTO.getAttachmentIds() != null) {
            // 1. 获取当前公告已有的附件
            List<NoticeAttachment> existing = noticeAttachmentMapper.selectByNoticeId(notice.getId());
            List<Long> existingIds = existing.stream().map(NoticeAttachment::getId).toList();
            List<Long> newIds = updateDTO.getAttachmentIds();

            // 2. 找出需要删除的附件（现有但不在新列表中的）
            List<Long> toDelete = existingIds.stream().filter(id -> !newIds.contains(id)).toList();
            for (Long id : toDelete) {
                NoticeAttachment att = noticeAttachmentMapper.selectById(id);
                if (att != null) {
                    // 删除物理文件
                    fileUploadUtil.deleteNoticeAttachment(id);
                    // 删除数据库记录
                    noticeAttachmentMapper.deleteById(id);
                }
            }

            // 3. 将新列表中的附件关联到当前公告（仅当尚未关联时）
            for (Long attId : newIds) {
                NoticeAttachment att = noticeAttachmentMapper.selectById(attId);
                if (att != null && att.getNoticeId() == null) {
                    att.setNoticeId(notice.getId());
                    noticeAttachmentMapper.updateById(att);
                }
            }
        }
    }

    @Override
    @Transactional
    public void publishNotice(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        if ("published".equals(notice.getStatus())) {
            throw new BusinessException("公告已是发布状态");
        }
        if (notice.getExpireTime() != null && notice.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("公告已过期");
        }
        notice.setStatus("published");
        notice.setPublishTime(LocalDateTime.now());
        noticeMapper.updateById(notice);
    }

    @Override
    @Transactional
    public void archiveNotice(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        if ("archived".equals(notice.getStatus())) {
            throw new BusinessException("公告已是下架状态");
        }
        notice.setStatus("archived");
        noticeMapper.updateById(notice);
    }

    @Override
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        // 物理删除（根据前端要求不可恢复）
        noticeMapper.deleteById(notice.getId());
        // 同时删除附件关联（可选）
        LambdaQueryWrapper<NoticeAttachment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeAttachment::getNoticeId, id);
        noticeAttachmentMapper.delete(wrapper);
        // 3. 删除用户阅读记录（所有用户对该公告的阅读记录）
        LambdaQueryWrapper<UserNoticeRead> readWrapper = new LambdaQueryWrapper<>();
        readWrapper.eq(UserNoticeRead::getNoticeId, notice.getId());
        userNoticeReadMapper.delete(readWrapper);
    }
}
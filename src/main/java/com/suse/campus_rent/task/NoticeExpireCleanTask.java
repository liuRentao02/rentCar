package com.suse.campus_rent.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.entity.Notice;
import com.suse.campus_rent.entity.UserNoticeRead;
import com.suse.campus_rent.mapper.NoticeMapper;
import com.suse.campus_rent.mapper.UserNoticeReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoticeExpireCleanTask {

    private final NoticeMapper noticeMapper;
    private final UserNoticeReadMapper readMapper;

    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    @Transactional
    public void cleanExpiredNoticeReads() {
        // 1. 查询所有已过期的公告ID
        List<Long> expiredIds = noticeMapper.selectList(
                new LambdaQueryWrapper<Notice>()
                        .isNotNull(Notice::getExpireTime)
                        .lt(Notice::getExpireTime, LocalDateTime.now())
                        .select(Notice::getId)
        ).stream().map(Notice::getId).toList();

        if (expiredIds.isEmpty()) return;
        else {
            readMapper.delete(new LambdaQueryWrapper<UserNoticeRead>()
                    .in(UserNoticeRead::getNoticeId, expiredIds));
        }
        log.info("清理过期公告读取记录完成，共处理 {} 条公告", expiredIds.size());
    }
}
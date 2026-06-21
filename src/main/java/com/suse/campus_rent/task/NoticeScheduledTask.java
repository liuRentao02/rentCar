package com.suse.campus_rent.task;

import com.suse.campus_rent.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeScheduledTask {

    private final NoticeMapper noticeMapper;

    /**
     * 定时扫描并发布公告
     * cron 表达式含义：每隔 1 分钟执行一次 (0 * * * * ?)
     * 你可以根据需求调整频率，比如每 5 分钟：0 0/5 * * * ?
     */
    @Scheduled(cron = "0 0 * * * *")
    public void publishScheduledNotices() {
        try {
            log.info("开始执行定时公告发布任务...");

            // 调用 Mapper 方法执行更新
            int count = noticeMapper.publishScheduledNotices();

            if (count > 0) {
                log.info("定时公告发布任务完成，成功发布了 {} 条公告", count);
            } else {
                log.info("定时公告发布任务完成，没有需要发布的公告");
            }
        } catch (Exception e) {
            log.error("定时公告发布任务执行失败", e);
        }
    }
}
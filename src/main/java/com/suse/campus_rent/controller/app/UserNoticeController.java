package com.suse.campus_rent.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.app.AppNoticeQueryDTO;
import com.suse.campus_rent.entity.Notice;
import com.suse.campus_rent.service.app.service.UserNoticeService;
import com.suse.campus_rent.vo.app.NoticeDetailVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class UserNoticeController {

    private final UserNoticeService noticeService;

    /**
     * 获取已发布公告列表（用户端）
     */
    @GetMapping
    public Result<IPage<Notice>> listNotices(@Valid AppNoticeQueryDTO queryDTO) {
        return Result.success(noticeService.listPublishedNotices(queryDTO));
    }

    /**
     * 获取公告详情
     */
    @GetMapping("/{id}")
    public Result<NoticeDetailVO> getNoticeDetail(@PathVariable Long id) {
        return Result.success(noticeService.getNoticeDetail(id));
    }

    /**
     * 已读设置
     */
    @PutMapping("/{id}")
    public Result<String> readNotice(@PathVariable Long id, @RequestParam("userId") Long userId) {
        return Result.success(noticeService.putReadNotice(id, userId));
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteNotice(@PathVariable Long id, @RequestParam("userId") Long userId) {
        return Result.success(noticeService.deleteNotice(id, userId));
    }
}
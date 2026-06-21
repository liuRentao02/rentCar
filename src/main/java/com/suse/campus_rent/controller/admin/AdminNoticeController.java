package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.NoticeCreateDTO;
import com.suse.campus_rent.dto.admin.NoticeQueryDTO;
import com.suse.campus_rent.dto.admin.NoticeUpdateDTO;
import com.suse.campus_rent.service.admin.service.NoticeService;
import com.suse.campus_rent.vo.admin.NoticeDetailVO;
import com.suse.campus_rent.vo.admin.NoticeListVO;
import com.suse.campus_rent.vo.admin.NoticeStatisticsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping("/statistics")
    public Result<NoticeStatisticsVO> getStatistics() {
        return Result.success(noticeService.getStatistics());
    }

    @GetMapping
    public Result<IPage<NoticeListVO>> listNotices(@Valid NoticeQueryDTO queryDTO) {
        IPage<NoticeListVO> page = noticeService.listNotices(queryDTO);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<NoticeDetailVO> getNoticeDetail(@PathVariable Long id) {
        return Result.success(noticeService.getNoticeDetail(id));
    }

    @OperLog(title = "新增公告", category = "NOTICE", level = "INFO")
    @PostMapping
    public Result<?> createNotice(@Valid @RequestBody NoticeCreateDTO createDTO) {
        noticeService.createNotice(createDTO);
        return Result.success("公告发布成功");
    }

    @OperLog(title = "修改公告", category = "NOTICE", level = "INFO")
    @PutMapping("/{id}")
    public Result<?> updateNotice(@PathVariable Long id, @Valid @RequestBody NoticeUpdateDTO updateDTO) {
        updateDTO.setId(id);
        noticeService.updateNotice(updateDTO);
        return Result.success("公告更新成功");
    }

    @OperLog(title = "发布公告", category = "NOTICE", level = "INFO")
    @PostMapping("/{id}/publish")
    public Result<?> publishNotice(@PathVariable Long id) {
        noticeService.publishNotice(id);
        return Result.success("公告已发布");
    }

    @OperLog(title = "下架公告", category = "NOTICE", level = "INFO")
    @PostMapping("/{id}/archive")
    public Result<?> archiveNotice(@PathVariable Long id) {
        noticeService.archiveNotice(id);
        return Result.success("公告已下架");
    }

    @OperLog(title = "删除公告", category = "NOTICE", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return Result.success("公告已删除");
    }
}

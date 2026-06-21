package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.ReviewBatchDeleteDTO;
import com.suse.campus_rent.dto.admin.ReviewQueryDTO;
import com.suse.campus_rent.dto.admin.ReviewStatusDTO;
import com.suse.campus_rent.service.admin.service.ReviewService;
import com.suse.campus_rent.vo.admin.ReviewDetailVO;
import com.suse.campus_rent.vo.admin.ReviewListVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    /**
     * 分页查询评论列表
     */
    @GetMapping
    public Result<IPage<ReviewListVO>> listReviews(ReviewQueryDTO queryDTO) {
        return Result.success(reviewService.listReviews(queryDTO));
    }

    /**
     * 获取评论详情
     */
    @GetMapping("/{id}")
    public Result<ReviewDetailVO> getReviewDetail(@PathVariable Long id) {
        return Result.success(reviewService.getReviewDetail(id));
    }

    /**
     * 修改评论状态（显示/隐藏）
     */
    @PutMapping("/status")
    public Result<?> updateStatus(@Valid @RequestBody ReviewStatusDTO dto) {
        reviewService.updateStatus(dto);
        return Result.success("操作成功");
    }

    /**
     * 删除评论（单条）
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除评论
     */
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@RequestBody ReviewBatchDeleteDTO dto) {
        reviewService.batchDelete(dto.getIds());
        return Result.success("批量删除成功");
    }

    /**
     * 批量隐藏/显示评论
     */
    @PutMapping("/batch-status")
    public Result<?> batchUpdateStatus(@RequestBody ReviewStatusDTO dto) {
        reviewService.batchUpdateStatus(dto.getIds(), dto.getStatus());
        return Result.success("批量操作成功");
    }
}
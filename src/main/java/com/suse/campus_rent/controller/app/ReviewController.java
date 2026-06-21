package com.suse.campus_rent.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.app.ReviewQueryDTO;
import com.suse.campus_rent.dto.app.ReviewSubmitDTO;
import com.suse.campus_rent.service.app.service.ReviewService;
import com.suse.campus_rent.vo.app.ReviewStatsVO;
import com.suse.campus_rent.vo.app.ReviewVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 获取评价统计 - 查询，不加日志
    @GetMapping("/stats")
    public Result<ReviewStatsVO> getStats() {
        return Result.success(reviewService.getStats());
    }

    // 查询评价列表 - 查询，不加日志
    @GetMapping
    public Result<Map<String, Object>> getReviews(ReviewQueryDTO query) {
        return Result.success(reviewService.getReviews(query));
    }

    @OperLog(title = "点赞评价", category = "REVIEW", level = "INFO")
    @PostMapping("/{id}/like")
    public Result<?> likeReview(@PathVariable Long id, @RequestParam Long userId) {
        reviewService.likeReview(id, userId);
        return Result.success();
    }

    @OperLog(title = "提交评价", category = "REVIEW", level = "INFO")
    @PostMapping
    public Result<?> submitReview(@RequestBody @Valid ReviewSubmitDTO dto,
                                  @RequestParam Long userId) {
        reviewService.submitReview(dto, userId);
        return Result.success("评价成功");
    }

    // 检查订单是否已评价 - 查询，不加日志
    @GetMapping("/check/{orderId}")
    public Result<Boolean> checkReviewed(@PathVariable Long orderId,
                                         @RequestParam Long userId) {
        boolean reviewed = reviewService.isOrderReviewed(orderId, userId);
        return Result.success(reviewed);
    }

    // 获取当前用户的所有评价 - 查询，不加日志
    @GetMapping("/my")
    public Result<IPage<ReviewVO>> getMyReviews(@RequestParam Long userId,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size) {
        IPage<ReviewVO> reviews = reviewService.getUserReviews(userId, page, size);
        return Result.success(reviews);
    }

    @OperLog(title = "修改评价状态", category = "REVIEW", level = "INFO")
    @PutMapping("/{id}/status")
    public Result<?> updateReviewStatus(@PathVariable Long id,
                                        @RequestParam Long userId,
                                        @RequestParam Integer status) {
        reviewService.updateReviewStatus(id, userId, status);
        return Result.success("状态更新成功");
    }

    @OperLog(title = "删除评价", category = "REVIEW", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteMyReview(@PathVariable Long id, @RequestParam Long userId) {
        reviewService.deleteReview(id, userId);
        return Result.success("删除成功");
    }
}
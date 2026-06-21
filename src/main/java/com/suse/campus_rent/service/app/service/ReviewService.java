package com.suse.campus_rent.service.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.app.ReviewQueryDTO;
import com.suse.campus_rent.dto.app.ReviewSubmitDTO;
import com.suse.campus_rent.vo.app.ReviewStatsVO;
import com.suse.campus_rent.vo.app.ReviewVO;

import java.util.Map;

public interface ReviewService {
    ReviewStatsVO getStats();

    Map<String, Object> getReviews(ReviewQueryDTO query);

    void likeReview(Long reviewId, Long userId);

    void submitReview(ReviewSubmitDTO dto, Long userId);

    boolean isOrderReviewed(Long orderId, Long userId);

    IPage<ReviewVO> getUserReviews(Long userId, Integer page, Integer size);

    void deleteReview(Long id, Long userId);

    void updateReviewStatus(Long id, Long userId, Integer status);
}
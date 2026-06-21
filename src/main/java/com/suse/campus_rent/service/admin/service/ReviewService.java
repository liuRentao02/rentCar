package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.ReviewQueryDTO;
import com.suse.campus_rent.dto.admin.ReviewStatusDTO;
import com.suse.campus_rent.vo.admin.ReviewDetailVO;
import com.suse.campus_rent.vo.admin.ReviewListVO;

import java.util.List;

public interface ReviewService {
    IPage<ReviewListVO> listReviews(ReviewQueryDTO queryDTO);

    ReviewDetailVO getReviewDetail(Long id);

    void updateStatus(ReviewStatusDTO dto);

    void deleteReview(Long id);

    void batchDelete(List<Long> ids);

    void batchUpdateStatus(List<Long> ids, Integer status);
}
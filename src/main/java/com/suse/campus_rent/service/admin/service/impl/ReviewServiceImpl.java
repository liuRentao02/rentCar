package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.ReviewQueryDTO;
import com.suse.campus_rent.dto.admin.ReviewStatusDTO;
import com.suse.campus_rent.service.admin.service.ReviewService;
import com.suse.campus_rent.vo.admin.ReviewDetailVO;
import com.suse.campus_rent.vo.admin.ReviewListVO;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.entity.Review;
import com.suse.campus_rent.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("adminReviewService")
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Override
    public IPage<ReviewListVO> listReviews(ReviewQueryDTO queryDTO) {
        Page<ReviewListVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return reviewMapper.selectReviewList(page, queryDTO);
    }

    @Override
    public ReviewDetailVO getReviewDetail(Long id) {
        ReviewDetailVO detail = reviewMapper.selectReviewDetail(id);
        if (detail == null) {
            throw new BusinessException("评论不存在");
        }
        String images = detail.getImages(); // 注意：ReviewDetailVO 中需要定义 getImages() 或 field
        if (StringUtils.hasText(images)) {
            List<String> urls = new ArrayList<>(Arrays.asList(images.split(",")));
            urls.replaceAll(String::trim);
            detail.setImageUrls(urls);
        } else {
            detail.setImageUrls(new ArrayList<>());
        }
        return detail;
    }

    @Override
    @Transactional
    public void updateStatus(ReviewStatusDTO dto) {
        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
            // 批量更新状态
            LambdaUpdateWrapper<Review> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(Review::getId, dto.getIds())
                    .set(Review::getStatus, dto.getStatus());
            reviewMapper.update(null, updateWrapper);
        } else if (dto.getId() != null) {
            // 单条更新
            Review review = reviewMapper.selectById(dto.getId());
            if (review == null) {
                throw new BusinessException("评论不存在");
            }
            if (review.getStatus().equals(dto.getStatus())) {
                return; // 状态未变化，无需更新
            }
            review.setStatus(dto.getStatus());
            reviewMapper.updateById(review);
        } else {
            throw new BusinessException("参数错误：未指定评论ID");
        }
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        if (id == null) {
            throw new BusinessException("评论不存在");
        }
        reviewMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("请选择要删除的评论");
        }
        reviewMapper.deleteByIds(ids);
    }

    @Override
    @Transactional
    public void batchUpdateStatus(List<Long> ids, Integer status) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("请选择要操作的评论");
        }
        LambdaUpdateWrapper<Review> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Review::getId, ids)
                .set(Review::getStatus, status);
        reviewMapper.update(null, updateWrapper);
    }
}
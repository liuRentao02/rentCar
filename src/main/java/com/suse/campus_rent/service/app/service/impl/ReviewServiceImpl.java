package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.OrderStatus;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.app.ReviewQueryDTO;
import com.suse.campus_rent.dto.app.ReviewSubmitDTO;
import com.suse.campus_rent.entity.*;
import com.suse.campus_rent.mapper.*;
import com.suse.campus_rent.service.app.service.ReviewService;
import com.suse.campus_rent.vo.app.ReviewStatsVO;
import com.suse.campus_rent.vo.app.ReviewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;
    private final CarInfoMapper carInfoMapper;    // 新表
    private final CarModelsMapper carModelsMapper; // 新表
    private final ReviewLikeMapper reviewLikeMapper;
    private final OrderMapper orderMapper;

    @Override
    public ReviewStatsVO getStats() {
        ReviewStatsVO stats = new ReviewStatsVO();

        // 总评价数（仅显示已发布的）
        Long totalReviews = reviewMapper.selectCount(
                new LambdaQueryWrapper<Review>().eq(Review::getStatus, 1));
        stats.setTotalReviews(totalReviews.intValue());

        // 平均分
        Double avg = reviewMapper.selectAvgRating();
        stats.setAvgRating(avg != null ? Double.parseDouble(String.format("%.1f", avg)) : 0.0);

        // 好评率 (rating >= 4)
        Long positiveCount = reviewMapper.selectCount(
                new LambdaQueryWrapper<Review>().eq(Review::getStatus, 1).ge(Review::getRating, 4));
        double rate = totalReviews == 0 ? 0 : (positiveCount * 100.0 / totalReviews);
        stats.setPositiveRate(Math.round(rate) + "%");

        // 已完成订单数
        long completedOrders = orderMapper.selectCount(
                new LambdaQueryWrapper<Order>().eq(Order::getStatus, OrderStatus.COMPLETED));
        stats.setTotalOrders(String.valueOf(completedOrders));

        return stats;
    }

    @Override
    public Map<String, Object> getReviews(ReviewQueryDTO query) {
        Page<Review> page = new Page<>(query.getPage(), query.getSize());

        QueryWrapper<Review> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);

        if (query.getRating() != null) {
            wrapper.eq("rating", query.getRating());
        }

        // 车型分类筛选（新表：car_models.category）
        if (query.getVehicle() != null && !query.getVehicle().isEmpty()) {
            List<Long> carIds = carInfoMapper.selectList(
                            new LambdaQueryWrapper<CarInfo>()
                                    .inSql(CarInfo::getModelId,
                                            "SELECT model_id FROM car_models WHERE category = '" + query.getVehicle() + "'"))
                    .stream().map(CarInfo::getCarId).collect(Collectors.toList());

            if (!carIds.isEmpty()) {
                wrapper.in("car_id", carIds);
            } else {
                return Map.of("records", Collections.emptyList(), "total", 0, "current", query.getPage(), "size", query.getSize());
            }
        }

        // 排序
        switch (query.getSortBy()) {
            case "highest" -> wrapper.orderByDesc("rating");
            case "helpful" -> wrapper.orderByDesc("likes_count");
            default -> wrapper.orderByDesc("create_time");
        }

        IPage<Review> reviewPage = reviewMapper.selectPage(page, wrapper);

        // 批量提取所有需要的 ID，一次性查出用户和车辆信息（消灭 N+1）
        Set<Long> userIds = new HashSet<>();
        Set<Long> carIds = new HashSet<>();
        for (Review r : reviewPage.getRecords()) {
            userIds.add(r.getUserId());
            carIds.add(r.getCarId());
        }

        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            userMapper.selectBatchIds(userIds).forEach(u -> userMap.put(u.getId(), u));
        }

        Map<Long, CarInfo> carMap = new HashMap<>();
        if (!carIds.isEmpty()) {
            carInfoMapper.selectBatchIds(carIds).forEach(c -> carMap.put(c.getCarId(), c));
        }

        // 批量查车型
        Set<Long> modelIds = carMap.values().stream()
                .map(CarInfo::getModelId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, CarModels> modelMap = new HashMap<>();
        if (!modelIds.isEmpty()) {
            carModelsMapper.selectBatchIds(modelIds).forEach(m -> modelMap.put(m.getModelId(), m));
        }

        // 组装 VO（不再查数据库）
        Map<Long, User> finalUserMap = userMap;
        Map<Long, CarInfo> finalCarMap = carMap;
        Map<Long, CarModels> finalModelMap = modelMap;

        List<ReviewVO> voList = reviewPage.getRecords().stream()
                .map(review -> convertToVO(review, finalUserMap, finalCarMap, finalModelMap))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", reviewPage.getTotal());
        result.put("current", reviewPage.getCurrent());
        result.put("size", reviewPage.getSize());
        return result;
    }

    private ReviewVO convertToVO(Review review, Map<Long, User> userMap,
                                 Map<Long, CarInfo> carMap, Map<Long, CarModels> modelMap) {
        ReviewVO vo = new ReviewVO();
        vo.setId(review.getId());

        // 用户信息（从缓存 Map 取，不查库）
        User user = userMap.get(review.getUserId());
        if (user != null) {
            vo.setName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            vo.setAvatar(user.getAvatar() != null ? user.getAvatar() : "https://randomuser.me/api/portraits/men/1.jpg");
            vo.setUserType(mapUserType(user.getRole()));
        } else {
            vo.setName("匿名用户");
            vo.setAvatar("https://randomuser.me/api/portraits/men/1.jpg");
            vo.setUserType("普通用户");
        }

        vo.setRating(review.getRating());

        // 车辆信息（从缓存 Map 取，不查库）
        CarInfo car = carMap.get(review.getCarId());
        if (car != null) {
            CarModels model = modelMap.get(car.getModelId());
            if (model != null) {
                vo.setCarModel(model.getBrandName() + " " + model.getModelName());
            }
        } else {
            vo.setCarModel("未知车型");
        }

        vo.setContent(review.getContent());
        vo.setDate(review.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        vo.setLikes(review.getLikesCount());
        vo.setComments(review.getCommentsCount());
        vo.setFeatured(review.getFeatured() == 1);
        vo.setLiked(false);
        vo.setImage(review.getImages());
        return vo;
    }

    private String mapUserType(String role) {
        if (role == null) return "普通用户";
        return switch (role) {
            case "student" -> "学生";
            case "admin" -> "管理员";
            default -> "普通用户";
        };
    }

    @Override
    @Transactional
    public void likeReview(Long reviewId, Long userId) {
        long count = reviewLikeMapper.selectCount(
                new LambdaQueryWrapper<ReviewLike>()
                        .eq(ReviewLike::getReviewId, reviewId)
                        .eq(ReviewLike::getUserId, userId));
        if (count > 0) return;

        ReviewLike like = new ReviewLike();
        like.setReviewId(reviewId);
        like.setUserId(userId);
        reviewLikeMapper.insert(like);

        Review review = reviewMapper.selectById(reviewId);
        if (review != null) {
            review.setLikesCount(review.getLikesCount() + 1);
            reviewMapper.updateById(review);
        }
    }

    @Override
    @Transactional
    public void submitReview(ReviewSubmitDTO dto, Long userId) {
        Order order = orderMapper.selectById(dto.getOrderId());
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在或无权限");
        }
        if (!OrderStatus.COMPLETED.equals(order.getStatus())) {
            throw new BusinessException("只有已完成的订单才能评价");
        }

        long existCount = reviewMapper.selectCount(
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getOrderId, dto.getOrderId())
                        .eq(Review::getUserId, userId));
        if (existCount > 0) {
            throw new BusinessException("您已评价过该订单");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setCarId(order.getCarId());
        review.setOrderId(dto.getOrderId());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setImages(dto.getImages());
        review.setStatus(1);
        review.setLikesCount(0);
        review.setCommentsCount(0);
        review.setFeatured(0);
        reviewMapper.insert(review);
    }

    @Override
    public boolean isOrderReviewed(Long orderId, Long userId) {
        return reviewMapper.selectCount(
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getOrderId, orderId)
                        .eq(Review::getUserId, userId)) > 0;
    }

    @Override
    public IPage<ReviewVO> getUserReviews(Long userId, Integer page, Integer size) {
        Page<Review> pageParam = new Page<>(page, size);
        IPage<Review> reviewPage = reviewMapper.selectPage(pageParam,
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getUserId, userId)
                        .orderByDesc(Review::getCreateTime));

        // 批量提取关联 ID（消灭 N+1）
        Set<Long> carIds = reviewPage.getRecords().stream()
                .map(Review::getCarId)
                .collect(Collectors.toSet());

        Map<Long, CarInfo> carMap = new HashMap<>();
        if (!carIds.isEmpty()) {
            carInfoMapper.selectBatchIds(carIds).forEach(c -> carMap.put(c.getCarId(), c));
        }

        // 组装 VO
        List<ReviewVO> voList = reviewPage.getRecords().stream().map(review -> {
            ReviewVO vo = new ReviewVO();
            vo.setId(review.getId());
            vo.setRating(review.getRating());
            vo.setContent(review.getContent());
            vo.setDate(review.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            vo.setLikes(review.getLikesCount());
            vo.setComments(review.getCommentsCount());
            vo.setFeatured(review.getFeatured() == 1);
            vo.setImage(review.getImages());
            vo.setStatus(review.getStatus());

            CarInfo car = carMap.get(review.getCarId());
            if (car != null) {
                CarModels carModels = carModelsMapper.selectById(car.getModelId());
                vo.setCarName(carModels.getBrandName());
                vo.setCarImage(getFirstImage(car.getImageUrls()));
            }
            return vo;
        }).collect(Collectors.toList());

        IPage<ReviewVO> voPage = new Page<>(reviewPage.getCurrent(), reviewPage.getSize(), reviewPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public void deleteReview(Long id, Long userId) {
        Review review = reviewMapper.selectById(id);
        if (review == null || !review.getUserId().equals(userId)) {
            throw new BusinessException("评价不存在或无权限");
        }
        reviewMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateReviewStatus(Long id, Long userId, Integer status) {
        Review review = reviewMapper.selectById(id);
        if (review == null || !review.getUserId().equals(userId)) {
            throw new BusinessException("评价不存在或无权限");
        }
        if (status != 0 && status != 1) {
            throw new BusinessException("状态值无效");
        }
        review.setStatus(status);
        reviewMapper.updateById(review);
    }

    private String getFirstImage(String imageUrls) {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return imageUrls.split(",")[0].trim();
        }
        return "";
    }
}

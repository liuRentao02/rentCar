package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.ReviewQueryDTO;
import com.suse.campus_rent.vo.admin.ReviewDetailVO;
import com.suse.campus_rent.vo.admin.ReviewListVO;
import com.suse.campus_rent.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    @Select("SELECT AVG(rating) FROM review WHERE status = 1")
    Double selectAvgRating();

    IPage<ReviewListVO> selectReviewList(Page<?> page, @Param("query") ReviewQueryDTO query);

    ReviewDetailVO selectReviewDetail(@Param("id") Long id);
}
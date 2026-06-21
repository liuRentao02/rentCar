package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suse.campus_rent.entity.ReviewLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评价点赞记录Mapper接口
 */
@Mapper
public interface ReviewLikeMapper extends BaseMapper<ReviewLike> {
}
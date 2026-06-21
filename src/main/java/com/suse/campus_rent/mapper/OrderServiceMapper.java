package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suse.campus_rent.entity.OrderServices;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单增值服务关联Mapper接口
 */
@Mapper
public interface OrderServiceMapper extends BaseMapper<OrderServices> {
}
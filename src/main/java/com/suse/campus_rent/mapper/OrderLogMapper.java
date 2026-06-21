package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suse.campus_rent.entity.OrderLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderLogMapper extends BaseMapper<OrderLog> {

    @Select("SELECT content, create_time as time FROM order_log WHERE order_id = #{orderId} ORDER BY create_time ")
    List<OrderLog> selectLogsByOrderId(@Param("orderId") Long orderId);
}
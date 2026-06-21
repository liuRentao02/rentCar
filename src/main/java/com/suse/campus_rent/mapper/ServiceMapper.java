package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.ServiceQueryDTO;
import com.suse.campus_rent.entity.Services;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ServiceMapper extends BaseMapper<Services> {

    IPage<Services> selectServicePage(Page<?> page, @Param("query") ServiceQueryDTO query);
}
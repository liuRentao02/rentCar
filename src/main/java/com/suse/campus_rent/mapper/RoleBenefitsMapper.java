package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.RoleBenefitsQueryDTO;
import com.suse.campus_rent.entity.RoleBenefits;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * RoleBenefitsMapper
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:25
 */
@Mapper
public interface RoleBenefitsMapper extends BaseMapper<RoleBenefits> {

    IPage<RoleBenefits> selectRoleBenefitsPage(Page<?> page, @Param("query") RoleBenefitsQueryDTO query);
}

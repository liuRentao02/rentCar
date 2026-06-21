package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.MaintenanceQueryDTO;
import com.suse.campus_rent.entity.MaintenanceRecord;
import com.suse.campus_rent.vo.admin.MaintenanceRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaintenanceRecordMapper extends BaseMapper<MaintenanceRecord> {

    /**
     * 分页查询维修记录，关联车辆信息
     */
    IPage<MaintenanceRecordVO> selectMaintenanceList(Page<?> page, @Param("query") MaintenanceQueryDTO query);
}
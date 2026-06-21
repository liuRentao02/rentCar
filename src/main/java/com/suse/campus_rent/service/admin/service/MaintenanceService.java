package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.MaintenanceCreateDTO;
import com.suse.campus_rent.dto.admin.MaintenanceQueryDTO;
import com.suse.campus_rent.dto.admin.MaintenanceUpdateDTO;
import com.suse.campus_rent.vo.admin.MaintenanceRecordVO;
import com.suse.campus_rent.vo.admin.VehicleOptionVO;

import java.util.List;

public interface MaintenanceService {
    /**
     * 获取车辆选项列表
     */
    List<VehicleOptionVO> getVehicleOptions();

    /**
     * 分页查询维修记录
     */
    IPage<MaintenanceRecordVO> listMaintenances(MaintenanceQueryDTO queryDTO);

    /**
     * 新增维修记录
     */
    void createMaintenance(MaintenanceCreateDTO createDTO);

    /**
     * 更新维修记录
     */
    void updateMaintenance(MaintenanceUpdateDTO updateDTO);

    /**
     * 删除维修记录
     */
    void deleteMaintenance(Long id);
}
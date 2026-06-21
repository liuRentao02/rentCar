package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.MaintenanceCreateDTO;
import com.suse.campus_rent.dto.admin.MaintenanceQueryDTO;
import com.suse.campus_rent.dto.admin.MaintenanceUpdateDTO;
import com.suse.campus_rent.entity.CarInfo;
import com.suse.campus_rent.mapper.CarInfoMapper;
import com.suse.campus_rent.service.admin.service.MaintenanceService;
import com.suse.campus_rent.vo.admin.MaintenanceRecordVO;
import com.suse.campus_rent.vo.admin.VehicleOptionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/maintenance")
@RequiredArgsConstructor
public class AdminMaintenanceController {

    private final MaintenanceService maintenanceService;
    private final CarInfoMapper carInfoMapper;

    @GetMapping("/vehicles")
    public Result<List<VehicleOptionVO>> getVehicleOptions() {
        return Result.success(maintenanceService.getVehicleOptions());
    }

    @GetMapping("/list")
    public Result<IPage<MaintenanceRecordVO>> listMaintenances(MaintenanceQueryDTO queryDTO) {
        return Result.success(maintenanceService.listMaintenances(queryDTO));
    }

    @OperLog(title = "新增维修记录", category = "MAINTENANCE", level = "INFO")
    @PostMapping
    public Result<?> createMaintenance(@Valid @RequestBody MaintenanceCreateDTO createDTO) {
        maintenanceService.createMaintenance(createDTO);

        // 【修复】：新增维修记录时，将车辆状态改为 "maintenance" (维修中)
        UpdateWrapper<CarInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("car_id", createDTO.getVehicleId())
                .set("status", "maintenance"); // 注意：这里改的是 status，不是 car_id！
        carInfoMapper.update(null, updateWrapper);

        return Result.success("维修记录添加成功");
    }

    @OperLog(title = "修改维修记录", category = "MAINTENANCE", level = "INFO")
    @PutMapping("/{id}")
    public Result<?> updateMaintenance(@PathVariable Long id, @Valid @RequestBody MaintenanceUpdateDTO updateDTO) {
        updateDTO.setId(id);
        maintenanceService.updateMaintenance(updateDTO);

        // 【修复】：防止空指针异常，并且安全地更新车辆状态
        if (updateDTO.getVehicleId() != null && updateDTO.getStatus() != null) {
            UpdateWrapper<CarInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("car_id", updateDTO.getVehicleId());

            // 判断维修是否完成，动态切换车辆状态
            if ("completed".equals(updateDTO.getStatus())) {
                updateWrapper.set("status", "available"); // 维修完成，状态切回可用
            } else {
                updateWrapper.set("status", "maintenance"); // 其他状态(进行中/待处理)，保持维修中
            }
            carInfoMapper.update(null, updateWrapper);
        }

        return Result.success("维修记录更新成功");
    }

    @OperLog(title = "删除维修记录", category = "MAINTENANCE", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return Result.success("维修记录删除成功");
    }
}

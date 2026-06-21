package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.MaintenanceCreateDTO;
import com.suse.campus_rent.dto.admin.MaintenanceQueryDTO;
import com.suse.campus_rent.dto.admin.MaintenanceUpdateDTO;
import com.suse.campus_rent.entity.MaintenanceRecord;
import com.suse.campus_rent.mapper.CarInfoMapper;
import com.suse.campus_rent.mapper.MaintenanceRecordMapper;
import com.suse.campus_rent.service.admin.service.MaintenanceService;
import com.suse.campus_rent.vo.admin.MaintenanceRecordVO;
import com.suse.campus_rent.vo.admin.VehicleOptionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service("adminMaintenanceService")
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRecordMapper maintenanceMapper;
    private final CarInfoMapper carInfoMapper;

    @Override
    public List<VehicleOptionVO> getVehicleOptions() {
        return carInfoMapper.selectVehicleOptions();
    }

    @Override
    public IPage<MaintenanceRecordVO> listMaintenances(MaintenanceQueryDTO queryDTO) {
        Page<MaintenanceRecordVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return maintenanceMapper.selectMaintenanceList(page, queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMaintenance(MaintenanceCreateDTO createDTO) {
        // 检查车辆是否存在
        if (carInfoMapper.selectById(createDTO.getVehicleId()) == null) {
            throw new BusinessException("车辆不存在");
        }

        MaintenanceRecord record = new MaintenanceRecord();
        BeanUtils.copyProperties(createDTO, record);
        maintenanceMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaintenance(MaintenanceUpdateDTO updateDTO) {
        MaintenanceRecord existing = maintenanceMapper.selectById(updateDTO.getId());
        if (existing == null) {
            throw new BusinessException("维修记录不存在");
        }

        // 如果修改了车辆ID，检查新车辆是否存在
        if (updateDTO.getVehicleId() != null && !updateDTO.getVehicleId().equals(existing.getVehicleId())) {
            if (carInfoMapper.selectById(updateDTO.getVehicleId()) == null) {
                throw new BusinessException("车辆不存在");
            }
        }

        // 只更新非空字段
        if (updateDTO.getVehicleId() != null) {
            existing.setVehicleId(updateDTO.getVehicleId());
        }
        if (StringUtils.hasText(updateDTO.getMaintenanceItem())) {
            existing.setMaintenanceItem(updateDTO.getMaintenanceItem());
        }
        if (updateDTO.getCost() != null) {
            existing.setCost(updateDTO.getCost());
        }
        if (updateDTO.getMaintenanceDate() != null) {
            existing.setMaintenanceDate(updateDTO.getMaintenanceDate());
        }
        if (StringUtils.hasText(updateDTO.getStatus())) {
            existing.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getRemarks() != null) { // 允许清空备注
            existing.setRemarks(updateDTO.getRemarks());
        }

        maintenanceMapper.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaintenance(Long id) {
        MaintenanceRecord existing = maintenanceMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("维修记录不存在");
        }
        maintenanceMapper.deleteById(id);
    }
}
package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.CarModelsCreateDTO;
import com.suse.campus_rent.dto.admin.CarModelsQueryDTO;
import com.suse.campus_rent.dto.admin.CarModelsUpdateDTO;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.entity.CarInfo;
import com.suse.campus_rent.entity.CarModels;
import com.suse.campus_rent.mapper.CarInfoMapper;
import com.suse.campus_rent.mapper.CarModelsMapper;
import com.suse.campus_rent.service.admin.service.CarModelsService;
import com.suse.campus_rent.vo.admin.CarModelsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("adminCarModelsService")
@RequiredArgsConstructor
public class CarModelsServiceImpl implements CarModelsService {

    private final CarModelsMapper carModelsMapper;
    private final CarInfoMapper carInfoMapper;

    @Override
    public IPage<CarModelsVO> listCarModels(CarModelsQueryDTO queryDTO) {
        Page<CarModels> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<CarModels> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getBrandName()), CarModels::getBrandName, queryDTO.getBrandName().trim())
                .like(StringUtils.hasText(queryDTO.getSeriesName()), CarModels::getSeriesName, queryDTO.getSeriesName().trim())
                .like(StringUtils.hasText(queryDTO.getModelName()), CarModels::getModelName, queryDTO.getModelName().trim());
        IPage<CarModels> modelPage = carModelsMapper.selectPage(page, wrapper);
        IPage<CarModelsVO> voPage = new Page<>(modelPage.getCurrent(), modelPage.getSize(), modelPage.getTotal());
        voPage.setRecords(modelPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public CarModelsVO getCarModel(Long id) {
        CarModels model = carModelsMapper.selectById(id);
        if (model == null) {
            throw new BusinessException("车型不存在");
        }
        return convertToVO(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCarModel(CarModelsCreateDTO createDTO) {
        checkUnique(createDTO.getBrandName(), createDTO.getModelName(), null);
        CarModels model = new CarModels();
        BeanUtils.copyProperties(createDTO, model);
        carModelsMapper.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCarModel(CarModelsUpdateDTO updateDTO) {
        CarModels existing = carModelsMapper.selectById(updateDTO.getModelId());
        if (existing == null) {
            throw new BusinessException("车型不存在");
        }

        // 如果品牌或型号发生变化，检查唯一性
        if ((updateDTO.getBrandName() != null && !updateDTO.getBrandName().equals(existing.getBrandName())) ||
                (updateDTO.getModelName() != null && !updateDTO.getModelName().equals(existing.getModelName()))) {
            checkUnique(
                    updateDTO.getBrandName() != null ? updateDTO.getBrandName() : existing.getBrandName(),
                    updateDTO.getModelName() != null ? updateDTO.getModelName() : existing.getModelName(),
                    updateDTO.getModelId()
            );
        }

        // 直接全量拷贝属性并更新
        BeanUtils.copyProperties(updateDTO, existing);
        carModelsMapper.updateById(existing);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCarModel(Long id) {
        CarModels existing = carModelsMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("车型不存在");
        }

        // 检查是否有车辆关联
        Long carCount = carInfoMapper.selectCount(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getModelId, id));
        if (carCount > 0) {
            throw new BusinessException("该车型下存在车辆，无法删除");
        }
        carModelsMapper.deleteById(existing.getModelId());
    }

    @Override
    public List<CarModels> getAllCarModel() {
        LambdaQueryWrapper<CarModels> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(CarModels::getBrandName, CarModels::getModelName);
        return carModelsMapper.selectList(wrapper);
    }

    // ========== 私有方法 ==========
    private CarModelsVO convertToVO(CarModels entity) {
        CarModelsVO vo = new CarModelsVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    private void checkUnique(String brand, String modelName, Long excludeId) {
        LambdaQueryWrapper<CarModels> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarModels::getBrandName, brand)
                .eq(CarModels::getModelName, modelName);
        if (excludeId != null) {
            wrapper.ne(CarModels::getModelId, excludeId);
        }
        if (carModelsMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该品牌下型号名称已存在");
        }
    }
}
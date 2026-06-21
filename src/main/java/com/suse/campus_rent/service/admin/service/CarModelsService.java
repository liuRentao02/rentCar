package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.CarModelsCreateDTO;
import com.suse.campus_rent.dto.admin.CarModelsQueryDTO;
import com.suse.campus_rent.dto.admin.CarModelsUpdateDTO;
import com.suse.campus_rent.entity.CarModels;
import com.suse.campus_rent.vo.admin.CarModelsVO;

import java.util.List;

public interface CarModelsService {
    IPage<CarModelsVO> listCarModels(CarModelsQueryDTO queryDTO);

    CarModelsVO getCarModel(Long id);

    void createCarModel(CarModelsCreateDTO createDTO);

    void updateCarModel(CarModelsUpdateDTO updateDTO);

    void deleteCarModel(Long id);

    List<CarModels> getAllCarModel();
}
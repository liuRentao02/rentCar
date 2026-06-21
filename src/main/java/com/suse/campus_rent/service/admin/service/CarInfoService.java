package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.CarInfoCreateDTO;
import com.suse.campus_rent.dto.admin.CarInfoQueryDTO;
import com.suse.campus_rent.dto.admin.CarInfoUpdateDTO;
import com.suse.campus_rent.vo.admin.CarInfoDetailVO;
import com.suse.campus_rent.vo.admin.CarInfoVO;

import java.util.List;

public interface CarInfoService {

    List<String> getAllBrands();

    IPage<CarInfoVO> listCars(CarInfoQueryDTO queryDTO);

    CarInfoDetailVO getCarDetail(Long carId);

    void createCar(CarInfoCreateDTO createDTO);

    void updateCar(CarInfoUpdateDTO updateDTO);

    void deleteCar(Long carId);
}

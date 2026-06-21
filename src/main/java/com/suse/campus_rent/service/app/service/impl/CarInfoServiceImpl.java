package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.mapper.CarInfoMapper;
import com.suse.campus_rent.service.app.service.CarInfoService;
import com.suse.campus_rent.vo.app.CarDetailVO;
import com.suse.campus_rent.vo.app.CarVO;
import com.suse.campus_rent.dto.app.FleetQueryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarInfoServiceImpl implements CarInfoService {

    private final CarInfoMapper carInfoMapper;

    @Override
    public List<CarVO> getHotCars() {
        List<CarVO> cars = carInfoMapper.selectHotCarsByOrders();
        // 如果订单统计为空（可能没有订单），则降级为随机取10辆
        if (cars == null || cars.isEmpty()) {
            cars = carInfoMapper.selectHotCars();
        }
        if (cars != null && !cars.isEmpty()) {
            cars.forEach(CarVO::buildSpecs);
        }
        return cars;
    }

    @Override
    public List<CarVO> getNewCars() {
        List<CarVO> cars = carInfoMapper.selectNewCars();
        if (cars != null && !cars.isEmpty()) {
            cars.forEach(CarVO::buildSpecs);
        }
        return cars;
    }

    @Override
    public IPage<CarVO> queryCarList(FleetQueryDTO query) {
        Page<CarVO> page = new Page<>(query.getPage(), query.getSize());
        IPage<CarVO> result = carInfoMapper.selectCarLists(page, query);
        result.getRecords().forEach(CarVO::buildSpecs);
        return result;
    }

    @Override
    public CarDetailVO getCarDetail(Long id) {
        // 1. 一步到位查出基础数据和关联的车型数据
        CarDetailVO detail = carInfoMapper.selectCarDetailApp(id);
        if (detail == null) {
            throw new BusinessException("车辆不存在或已下架");
        }
        detail.buildDetail();
        return detail;
    }

}
package com.suse.campus_rent.service.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.app.FleetQueryDTO;
import com.suse.campus_rent.vo.app.CarDetailVO;
import com.suse.campus_rent.vo.app.CarVO;

import java.util.List;

/**
 * CarInfoService
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/4 13:31
 */
public interface CarInfoService {
    List<CarVO> getHotCars();

    List<CarVO> getNewCars();

    IPage<CarVO> queryCarList(FleetQueryDTO query);

    CarDetailVO getCarDetail(Long id);
}

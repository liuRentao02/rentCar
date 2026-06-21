package com.suse.campus_rent.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.service.app.service.CarInfoService;
import com.suse.campus_rent.vo.app.CarDetailVO;
import com.suse.campus_rent.vo.app.CarVO;
import com.suse.campus_rent.dto.app.FleetQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {


    private final CarInfoService carInfoService;

    @GetMapping("/hot")
    public Result<List<CarVO>> getHotCars() {
        List<CarVO> list = carInfoService.getHotCars();
        return Result.success(list);
    }

    @GetMapping("/new")
    public Result<List<CarVO>> getNewCars() {
        List<CarVO> list = carInfoService.getNewCars();
        return Result.success(list);
    }

    @GetMapping("/list")
    public Result<IPage<CarVO>> getCarList(FleetQueryDTO query) {
        IPage<CarVO> page = carInfoService.queryCarList(query);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<CarDetailVO> getCarDetail(@PathVariable Long id) {
        CarDetailVO detail = carInfoService.getCarDetail(id);
        return Result.success(detail);
    }
}
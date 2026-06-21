package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.CarInfoCreateDTO;
import com.suse.campus_rent.dto.admin.CarInfoQueryDTO;
import com.suse.campus_rent.dto.admin.CarInfoUpdateDTO;
import com.suse.campus_rent.service.admin.service.CarInfoService;
import com.suse.campus_rent.vo.admin.CarInfoDetailVO;
import com.suse.campus_rent.vo.admin.CarInfoVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/vehicle")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminCarInfoController {

    private final CarInfoService adminCarInfoService;

    /**
     * 获取所有品牌列表
     */
    @GetMapping("/brands")
    public Result<List<String>> getBrands() {
        return Result.success(adminCarInfoService.getAllBrands());
    }

    /**
     * 分页查询车辆列表
     */
    @PostMapping("/list")
    public Result<IPage<CarInfoVO>> listCars(@RequestBody CarInfoQueryDTO queryDTO) {
        IPage<CarInfoVO> page = adminCarInfoService.listCars(queryDTO);
        return Result.success(page);
    }

    /**
     * 获取车辆详情
     */
    @GetMapping("/{id}")
    public Result<CarInfoDetailVO> getCarDetail(@PathVariable Long id) {
        return Result.success(adminCarInfoService.getCarDetail(id));
    }

    /**
     * 新增车辆
     */
    @OperLog(title = "新增车辆", category = "VEHICLE")
    @PostMapping
    public Result<?> createCar(@Valid @RequestBody CarInfoCreateDTO createDTO) {
        adminCarInfoService.createCar(createDTO);
        return Result.success("车辆添加成功");
    }

    /**
     * 更新车辆
     */
    @OperLog(title = "修改车辆", category = "VEHICLE")
    @PutMapping
    public Result<?> updateCar(@Valid @RequestBody CarInfoUpdateDTO updateDTO) {
        adminCarInfoService.updateCar(updateDTO);
        return Result.success("车辆更新成功");
    }

    /**
     * 删除车辆
     */
    @OperLog(title = "删除车辆", category = "VEHICLE", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteCar(@PathVariable Long id) {
        adminCarInfoService.deleteCar(id);
        return Result.success("车辆删除成功");
    }
}
package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.dto.admin.CarModelsCreateDTO;
import com.suse.campus_rent.dto.admin.CarModelsQueryDTO;
import com.suse.campus_rent.dto.admin.CarModelsUpdateDTO;
import com.suse.campus_rent.entity.CarModels;
import com.suse.campus_rent.service.admin.service.CarModelsService;
import com.suse.campus_rent.vo.admin.CarModelsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/car-models")
@RequiredArgsConstructor
public class AdminCarModelsController {
    private final CarModelsService carModelsService;

    /**
     * 获取所有车型配置
     */
    @GetMapping("/all")
    public Result<List<CarModels>> getAllModels() {
        return Result.success(carModelsService.getAllCarModel());
    }

    @GetMapping
    public Result<IPage<CarModelsVO>> listCarModels(CarModelsQueryDTO queryDTO) {
        return Result.success(carModelsService.listCarModels(queryDTO));
    }

    @GetMapping("/{id}")
    public Result<CarModelsVO> getCarModel(@PathVariable Long id) {
        return Result.success(carModelsService.getCarModel(id));
    }

    @OperLog(title = "新增车辆型号", category = "VEHICLE")
    @PostMapping
    public Result<?> createCarModel(@Valid @RequestBody CarModelsCreateDTO createDTO) {
        carModelsService.createCarModel(createDTO);
        return Result.success("新增成功");
    }

    @OperLog(title = "修改车辆型号", category = "VEHICLE")
    @PutMapping
    public Result<?> updateCarModel(@RequestBody CarModelsUpdateDTO updateDTO) {
        carModelsService.updateCarModel(updateDTO);
        return Result.success("更新成功");
    }

    @OperLog(title = "删除车辆型号", category = "VEHICLE", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteCarModel(@PathVariable Long id) {
        carModelsService.deleteCarModel(id);
        return Result.success("删除成功");
    }
}

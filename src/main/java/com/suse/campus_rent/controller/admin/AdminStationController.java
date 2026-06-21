package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.StationCreateDTO;
import com.suse.campus_rent.dto.admin.StationQueryDTO;
import com.suse.campus_rent.dto.admin.StationUpdateDTO;
import com.suse.campus_rent.service.admin.service.StationService;
import com.suse.campus_rent.vo.admin.StationVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/stations")
@RequiredArgsConstructor
public class AdminStationController {

    private final StationService stationService;

    @GetMapping
    public Result<IPage<StationVO>> listStations(StationQueryDTO queryDTO) {
        return Result.success(stationService.listStations(queryDTO));
    }

    @GetMapping("/{id}")
    public Result<StationVO> getStation(@PathVariable Long id) {
        return Result.success(stationService.getStation(id));
    }

    @OperLog(title = "新增门店", category = "STATION", level = "INFO")
    @PostMapping
    public Result<?> createStation(@Valid @RequestBody StationCreateDTO createDTO) {
        stationService.createStation(createDTO);
        return Result.success("新增成功");
    }

    @OperLog(title = "修改门店", category = "STATION", level = "INFO")
    @PutMapping("/{id}")
    public Result<?> updateStation(@PathVariable Long id, @Valid @RequestBody StationUpdateDTO updateDTO) {
        updateDTO.setId(id);
        stationService.updateStation(updateDTO);
        return Result.success("更新成功");
    }

    @OperLog(title = "删除门店", category = "STATION", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return Result.success("删除成功");
    }

    @OperLog(title = "修改门店状态", category = "STATION", level = "INFO")
    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        stationService.updateStatus(id, status);
        return Result.success("状态更新成功");
    }
}

package com.suse.campus_rent.controller.app;

import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.entity.Station;
import com.suse.campus_rent.mapper.StationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationMapper stationMapper;

    @GetMapping("/active")
    public Result<List<Station>> getActiveStations() {
        return Result.success(stationMapper.selectActiveStations());
    }
}
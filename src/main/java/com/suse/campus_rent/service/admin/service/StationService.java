package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.StationCreateDTO;
import com.suse.campus_rent.dto.admin.StationQueryDTO;
import com.suse.campus_rent.dto.admin.StationUpdateDTO;
import com.suse.campus_rent.vo.admin.StationVO;

public interface StationService {
    IPage<StationVO> listStations(StationQueryDTO queryDTO);

    StationVO getStation(Long id);

    void createStation(StationCreateDTO createDTO);

    void updateStation(StationUpdateDTO updateDTO);

    void deleteStation(Long id);

    void updateStatus(Long id, Integer status);
}
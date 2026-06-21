package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.StationCreateDTO;
import com.suse.campus_rent.dto.admin.StationQueryDTO;
import com.suse.campus_rent.dto.admin.StationUpdateDTO;
import com.suse.campus_rent.entity.Station;
import com.suse.campus_rent.mapper.StationMapper;
import com.suse.campus_rent.service.admin.service.StationService;
import com.suse.campus_rent.vo.admin.StationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Slf4j
@Service("adminStationService")
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationMapper stationMapper;

    @Override
    public IPage<StationVO> listStations(StationQueryDTO queryDTO) {
        Page<Station> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<Station> stationPage = stationMapper.selectStationPage(page, queryDTO);
        IPage<StationVO> voPage = new Page<>(stationPage.getCurrent(), stationPage.getSize(), stationPage.getTotal());
        voPage.setRecords(stationPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public StationVO getStation(Long id) {
        Station station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException("网点不存在");
        }
        return convertToVO(station);
    }

    @Override
    @Transactional
    public void createStation(StationCreateDTO createDTO) {
        Station station = new Station();
        BeanUtils.copyProperties(createDTO, station);
        stationMapper.insert(station);
    }

    @Override
    @Transactional
    public void updateStation(StationUpdateDTO updateDTO) {
        Station existing = stationMapper.selectById(updateDTO.getId());
        if (existing == null) {
            throw new BusinessException("网点不存在");
        }
        // 只更新非空字段
        if (StringUtils.hasText(updateDTO.getName())) {
            existing.setName(updateDTO.getName());
        }
        if (StringUtils.hasText(updateDTO.getAddress())) {
            existing.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getLatitude() != null) {
            existing.setLatitude(updateDTO.getLatitude());
        }
        if (updateDTO.getLongitude() != null) {
            existing.setLongitude(updateDTO.getLongitude());
        }
        if (StringUtils.hasText(updateDTO.getContactPhone())) {
            existing.setContactPhone(updateDTO.getContactPhone());
        }
        if (StringUtils.hasText(updateDTO.getBusinessHours())) {
            existing.setBusinessHours(updateDTO.getBusinessHours());
        }
        if (updateDTO.getStatus() != null) {
            existing.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getSortOrder() != null) {
            existing.setSortOrder(updateDTO.getSortOrder());
        }
        log.info("station: {}", existing);
        stationMapper.updateById(existing);
    }

    @Override
    @Transactional
    public void deleteStation(Long id) {
        Station station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException("网点不存在");
        }
        stationMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Station station = stationMapper.selectById(id);
        if (station == null) {
            throw new BusinessException("网点不存在");
        }
        station.setStatus(status);
        stationMapper.updateById(station);
    }

    private StationVO convertToVO(Station entity) {
        StationVO vo = new StationVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
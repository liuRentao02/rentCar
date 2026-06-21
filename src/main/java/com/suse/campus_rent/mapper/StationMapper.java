package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.StationQueryDTO;
import com.suse.campus_rent.entity.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StationMapper extends BaseMapper<Station> {

    IPage<Station> selectStationPage(Page<?> page, @Param("query") StationQueryDTO query);

    List<Station> selectActiveStations();
}
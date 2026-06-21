package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.CarInfoQueryDTO;
import com.suse.campus_rent.dto.app.FleetQueryDTO;
import com.suse.campus_rent.entity.CarInfo;
import com.suse.campus_rent.vo.app.CarDetailVO;
import com.suse.campus_rent.vo.app.CarVO;
import com.suse.campus_rent.vo.admin.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * CarInfoMapper
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/3 16:59
 */
@Mapper
public interface CarInfoMapper extends BaseMapper<CarInfo> {
    /**
     * 分页查询车辆列表（关联 car_model 表获取品牌、车型名、能源类型）
     */
    IPage<CarInfoVO> selectCarInfoList(Page<?> page, @Param("query") CarInfoQueryDTO query);

    /**
     * 获取所有品牌（从 car_model 去重）
     */
    @Select("SELECT DISTINCT brand_name FROM car_models ORDER BY brand_name")
    List<String> selectAllBrands();

    /**
     * 查询车辆详情（关联 car_model）
     */
    CarInfoDetailVO selectCarInfoDetail(@Param("carId") Long carId);

    /**
     * 获取车辆下拉选项
     */
    @Select("SELECT ci.car_id AS value, CONCAT(ci.plate_number, ' (', cm.model_name, ')') AS label " +
            "FROM car_info ci LEFT JOIN car_models cm ON ci.model_id = cm.model_id " +
            "ORDER BY ci.plate_number")
    List<VehicleOptionVO> selectVehicleOptions();

    /**
     * 获取车辆总数
     */
    @Select("SELECT COUNT(*) FROM car_info")
    Integer selectTotalCars();

    /**
     * 获取当前在租车辆数（状态为 rented）
     */
    @Select("SELECT COUNT(*) FROM car_info WHERE status = 'rented'")
    Integer selectRentedCars();


    List<CarVO> selectNewCars();

    List<CarVO> selectHotCars();

    List<CarVO> selectHotCarsByOrders();

    IPage<CarVO> selectCarLists(Page<CarVO> page, FleetQueryDTO query);

    CarDetailVO selectCarDetailApp(@Param("id") Long id);
}

package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CarInfoDetailVO {
    private Long carId;
    private Long modelId;
    private String plateNumber;
    private String vinCode;
    private String engineNo;
    private String vehicleColor;
    private Long shopId;
    private Integer currentMileage;
    private BigDecimal currentFuel;
    private LocalDate licenseDate;
    private LocalDate insuranceExpiry;
    private LocalDate inspectionExpiry;
    private BigDecimal dailyRent;
    private BigDecimal depositAmount;
    private Integer rentalCount;
    private String status;
    private String imageUrls;
    private List<String> imageList;  // 拆分后的图片列表
    private String image;            // 主图

    private String brandName;//品牌
    private String seriesName;//车系
    private String modelName;//型号
    private String category;//类别
    private String energyType;//能源类型
    private String gearboxType;//变速箱
    private String driveMode;//驱动方式
    private String seatCount;//座位数
    private String sunroofType;//天窗
    private String interiorColor;//内饰
    private String displacement;//排量
    private String fuelGrade;//燃油标号
    private String emissionStandard;//排放标准
}

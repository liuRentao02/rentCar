package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.CarInfoCreateDTO;
import com.suse.campus_rent.dto.admin.CarInfoQueryDTO;
import com.suse.campus_rent.dto.admin.CarInfoUpdateDTO;
import com.suse.campus_rent.entity.CarInfo;
import com.suse.campus_rent.mapper.CarInfoMapper;
import com.suse.campus_rent.service.admin.service.CarInfoService;
import com.suse.campus_rent.vo.admin.CarInfoDetailVO;
import com.suse.campus_rent.vo.admin.CarInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

@Slf4j
@Service("adminCarInfoService")
@RequiredArgsConstructor
public class CarInfoServiceImpl implements CarInfoService {

    @Value("${file.upload.path}")
    private String uploadPath;   // 例如 D:/campus-rental/uploads/

    @Value("${file.access.path}")
    private String accessPath;   // 例如 /uploads/

    private final CarInfoMapper carInfoMapper;

    @Override
    public List<String> getAllBrands() {
        return carInfoMapper.selectAllBrands();
    }

    @Override
    public IPage<CarInfoVO> listCars(CarInfoQueryDTO queryDTO) {
        Page<CarInfoVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return carInfoMapper.selectCarInfoList(page, queryDTO);
    }

    @Override
    public CarInfoDetailVO getCarDetail(Long carId) {
        CarInfoDetailVO detailVO = carInfoMapper.selectCarInfoDetail(carId);
        if (detailVO == null) {
            throw new BusinessException("车辆不存在");
        }
        // 拆分图片列表
        if (StringUtils.hasText(detailVO.getImageUrls())) {
            List<String> imageList = Arrays.asList(detailVO.getImageUrls().split(","));
            detailVO.setImageList(imageList);
        }
        // 如果主图未设置，则取第一个
        if (detailVO.getImage() == null && detailVO.getImageList() != null && !detailVO.getImageList().isEmpty()) {
            detailVO.setImage(detailVO.getImageList().get(0));
        }
        return detailVO;
    }

    @Override
    @Transactional
    public void createCar(CarInfoCreateDTO createDTO) {
        log.info("新增车辆:{}", createDTO);

        // 校验车牌号唯一
        checkPlateUnique(createDTO.getPlateNumber(), null);
        // 校验车架号唯一
        checkVinUnique(createDTO.getVinCode(), null);

        CarInfo carInfo = new CarInfo();
        carInfo.setModelId(createDTO.getModelId());
        carInfo.setPlateNumber(createDTO.getPlateNumber());
        carInfo.setVinCode(createDTO.getVinCode());
        carInfo.setEngineNo(createDTO.getEngineNo());
        carInfo.setVehicleColor(createDTO.getVehicleColor());
        carInfo.setShopId(createDTO.getShopId());
        carInfo.setCurrentMileage(createDTO.getCurrentMileage() != null ? createDTO.getCurrentMileage() : 0);
        carInfo.setCurrentFuel(createDTO.getCurrentFuel());
        carInfo.setLicenseDate(createDTO.getLicenseDate());
        carInfo.setInsuranceExpiry(createDTO.getInsuranceExpiry());
        carInfo.setInspectionExpiry(createDTO.getInspectionExpiry());
        carInfo.setDailyRent(createDTO.getDailyRent());
        carInfo.setDepositAmount(createDTO.getDepositAmount());
        carInfo.setStatus(StringUtils.hasText(createDTO.getStatus()) ? createDTO.getStatus() : "available");
        carInfo.setImageUrls(createDTO.getImageUrls());

        carInfoMapper.insert(carInfo);
    }

    @Override
    @Transactional
    public void updateCar(CarInfoUpdateDTO updateDTO) {
        log.info("updateDTO:{}", updateDTO);

        CarInfo carInfo = carInfoMapper.selectById(updateDTO.getCarId());
        if (carInfo == null) {
            throw new BusinessException("车辆不存在");
        }

        // 校验车牌号唯一（排除自身）
        if (StringUtils.hasText(updateDTO.getPlateNumber())) {
            checkPlateUnique(updateDTO.getPlateNumber(), updateDTO.getCarId());
        }
        // 校验车架号唯一（排除自身）
        if (StringUtils.hasText(updateDTO.getVinCode())) {
            checkVinUnique(updateDTO.getVinCode(), updateDTO.getCarId());
        }

        CarInfo updateEntity = new CarInfo();
        updateEntity.setCarId(updateDTO.getCarId());

        if (updateDTO.getModelId() != null) {
            updateEntity.setModelId(updateDTO.getModelId());
        }
        if (StringUtils.hasText(updateDTO.getPlateNumber())) {
            updateEntity.setPlateNumber(updateDTO.getPlateNumber());
        }
        if (StringUtils.hasText(updateDTO.getVinCode())) {
            updateEntity.setVinCode(updateDTO.getVinCode());
        }
        if (StringUtils.hasText(updateDTO.getEngineNo())) {
            updateEntity.setEngineNo(updateDTO.getEngineNo());
        }
        if (StringUtils.hasText(updateDTO.getVehicleColor())) {
            updateEntity.setVehicleColor(updateDTO.getVehicleColor());
        }
        if (updateDTO.getShopId() != null) {
            updateEntity.setShopId(updateDTO.getShopId());
        }
        if (updateDTO.getCurrentMileage() != null) {
            updateEntity.setCurrentMileage(updateDTO.getCurrentMileage());
        }
        if (updateDTO.getCurrentFuel() != null) {
            updateEntity.setCurrentFuel(updateDTO.getCurrentFuel());
        }
        if (updateDTO.getLicenseDate() != null) {
            updateEntity.setLicenseDate(updateDTO.getLicenseDate());
        }
        if (updateDTO.getInsuranceExpiry() != null) {
            updateEntity.setInsuranceExpiry(updateDTO.getInsuranceExpiry());
        }
        if (updateDTO.getInspectionExpiry() != null) {
            updateEntity.setInspectionExpiry(updateDTO.getInspectionExpiry());
        }
        if (updateDTO.getDailyRent() != null) {
            updateEntity.setDailyRent(updateDTO.getDailyRent());
        }
        if (updateDTO.getDepositAmount() != null) {
            updateEntity.setDepositAmount(updateDTO.getDepositAmount());
        }
        if (StringUtils.hasText(updateDTO.getStatus())) {
            updateEntity.setStatus(updateDTO.getStatus());
            log.info("更新车辆状态: {}", updateDTO.getStatus());
        }
        if (StringUtils.hasText(updateDTO.getImageUrls())) {
            updateEntity.setImageUrls(updateDTO.getImageUrls());
        }

        // ========== 处理旧图片物理删除 ==========
        String oldImageUrls = carInfo.getImageUrls();
        String newImageUrls = updateDTO.getImageUrls();
        deleteRemovedImages(oldImageUrls, newImageUrls);
        // ====================================

        carInfoMapper.updateById(updateEntity);
    }

    @Override
    @Transactional
    public void deleteCar(Long carId) {
        CarInfo carInfo = carInfoMapper.selectById(carId);
        if (carInfo == null) {
            throw new BusinessException("车辆不存在");
        }
        carInfoMapper.deleteById(carId);
    }

    private void checkPlateUnique(String plateNumber, Long excludeId) {
        LambdaQueryWrapper<CarInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarInfo::getPlateNumber, plateNumber);
        if (excludeId != null) {
            wrapper.ne(CarInfo::getCarId, excludeId);
        }
        if (carInfoMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("车牌号已存在");
        }
    }

    private void checkVinUnique(String vinCode, Long excludeId) {
        LambdaQueryWrapper<CarInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarInfo::getVinCode, vinCode);
        if (excludeId != null) {
            wrapper.ne(CarInfo::getCarId, excludeId);
        }
        if (carInfoMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("车架号/VIN已存在");
        }
    }

    private void deleteRemovedImages(String oldUrls, String newUrls) {
        if (!StringUtils.hasText(oldUrls)) {
            return;
        }
        Set<String> oldSet = new HashSet<>(Arrays.asList(oldUrls.split(",")));
        Set<String> newSet = StringUtils.hasText(newUrls)
                ? new HashSet<>(Arrays.asList(newUrls.split(",")))
                : Collections.emptySet();

        // 计算需要删除的 URL（在旧集合但不在新集合中）
        oldSet.removeAll(newSet);
        for (String url : oldSet) {
            deleteImageFileIfExists(url);
        }
    }

    private void deleteImageFileIfExists(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) return;
        try {
            String relativePath = extractRelativePathFromUrl(imageUrl);
            if (relativePath == null) {
                log.warn("无法提取相对路径，跳过删除: {}", imageUrl);
                return;
            }
            // 拼接完整物理路径
            File file = new File(uploadPath, relativePath);
            if (file.exists()) {
                if (file.delete()) {
                    log.info("删除旧图片成功: {}", file.getAbsolutePath());
                } else {
                    log.warn("删除旧图片失败: {}", file.getAbsolutePath());
                }
            } else {
                log.warn("旧图片文件不存在: {}", file.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error("删除图片异常, URL: {}", imageUrl, e);
        }
    }

    private String extractRelativePathFromUrl(String imageUrl) {
        String prefix = accessPath;
        if (!StringUtils.hasText(prefix)) {
            prefix = "/uploads/";
        }
        int idx = imageUrl.indexOf(prefix);
        if (idx == -1) {
            // 兼容无域名的相对路径
            idx = imageUrl.indexOf(prefix.replaceFirst("^/", ""));
        }
        if (idx == -1) return null;
        String relative = imageUrl.substring(idx + prefix.length());
        // 去除查询参数
        int qIdx = relative.indexOf('?');
        if (qIdx != -1) relative = relative.substring(0, qIdx);
        return relative;
    }
}

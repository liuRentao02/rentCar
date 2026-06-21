package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.entity.SystemConfiguration;
import com.suse.campus_rent.mapper.SystemConfigurationMapper;
import com.suse.campus_rent.service.app.service.SystemConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SystemConfigurationServiceImpl
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:33
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private final SystemConfigurationMapper systemMapping;

    @Override
    public Map<String, String> init() {
        LambdaQueryWrapper<SystemConfiguration> qw = new LambdaQueryWrapper<>();
        qw.le(SystemConfiguration::getExpireTime, LocalDateTime.now())
                .or()
                .isNull(SystemConfiguration::getEffectiveTime)
                .or()
                .isNull(SystemConfiguration::getExpireTime);

        List<SystemConfiguration> systemConfigurations = systemMapping.selectList(qw);
        log.info("sys{}", systemConfigurations);
        return toSystemVo(systemConfigurations);
    }

    public Map<String, String> toSystemVo(List<SystemConfiguration> sys) {
        Map<String, String> res = sys.stream()
                .collect(Collectors.toMap(SystemConfiguration::getConfigKey, SystemConfiguration::getConfigValue));
        return res;
    }
}

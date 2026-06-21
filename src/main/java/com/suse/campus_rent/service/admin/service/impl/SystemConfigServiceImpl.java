package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.common.SystemConfigKeys;
import com.suse.campus_rent.entity.SystemConfiguration;
import com.suse.campus_rent.mapper.SystemConfigurationMapper;
import com.suse.campus_rent.service.admin.service.SystemConfigService;
import com.suse.campus_rent.vo.admin.SystemConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("adminSystemConfigService")
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigurationMapper configMapper;

    // 默认配置值（仅保留平台信息和预警）
    private static final Map<String, String> DEFAULT_CONFIG = Map.ofEntries(
            Map.entry(SystemConfigKeys.PLATFORM_NAME, "QuickWheels 校园租车平台"),
            Map.entry(SystemConfigKeys.PLATFORM_TEXT, "致力于为用户提供最便捷、最安全的租车服务。"),
            Map.entry(SystemConfigKeys.PLATFORM_URL, "https://quickwheels.edu.cn"),
            Map.entry(SystemConfigKeys.PLATFORM_EMAIL, "support@quickwheels.edu.cn"),
            Map.entry(SystemConfigKeys.PLATFORM_PHONE, "400-888-9999"),
            Map.entry(SystemConfigKeys.PLATFORM_ADDRESS, "北京市朝阳区科技园路88号 QuickWheels大厦 12层"),
            Map.entry(SystemConfigKeys.PLATFORM_WORK_TIME, "周一至周日 8:00 - 22:00"),
            Map.entry(SystemConfigKeys.FOOTER_TEXT, "© 2026 QuickWheels 校园租车平台"),
            Map.entry(SystemConfigKeys.LOGO, "")
    );

    @Override
    public SystemConfigVO getConfig() {
        LambdaQueryWrapper<SystemConfiguration> wrapper = new LambdaQueryWrapper<>();
        List<SystemConfiguration> list = configMapper.selectList(wrapper);
        Map<String, String> configMap = list.stream()
                .collect(Collectors.toMap(SystemConfiguration::getConfigKey, SystemConfiguration::getConfigValue));

        SystemConfigVO vo = new SystemConfigVO();
        vo.setPlatformName(getValue(configMap, SystemConfigKeys.PLATFORM_NAME));
        vo.setPlatformText(getValue(configMap, SystemConfigKeys.PLATFORM_TEXT));
        vo.setPlatformUrl(getValue(configMap, SystemConfigKeys.PLATFORM_URL));
        vo.setPlatformEmail(getValue(configMap, SystemConfigKeys.PLATFORM_EMAIL));
        vo.setPlatformPhone(getValue(configMap, SystemConfigKeys.PLATFORM_PHONE));
        vo.setPlatformAddress(getValue(configMap, SystemConfigKeys.PLATFORM_ADDRESS));
        vo.setPlatformWorkTime(getValue(configMap, SystemConfigKeys.PLATFORM_WORK_TIME));
        vo.setFooterText(getValue(configMap, SystemConfigKeys.FOOTER_TEXT));
        vo.setLogo(getValue(configMap, SystemConfigKeys.LOGO));

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(SystemConfigVO configVO) {
        // 平台信息
        saveOrUpdate(SystemConfigKeys.PLATFORM_NAME, configVO.getPlatformName());
        saveOrUpdate(SystemConfigKeys.PLATFORM_TEXT, configVO.getPlatformText());
        saveOrUpdate(SystemConfigKeys.PLATFORM_URL, configVO.getPlatformUrl());
        saveOrUpdate(SystemConfigKeys.PLATFORM_EMAIL, configVO.getPlatformEmail());
        saveOrUpdate(SystemConfigKeys.PLATFORM_PHONE, configVO.getPlatformPhone());
        saveOrUpdate(SystemConfigKeys.PLATFORM_ADDRESS, configVO.getPlatformAddress());
        saveOrUpdate(SystemConfigKeys.PLATFORM_WORK_TIME, configVO.getPlatformWorkTime());
        saveOrUpdate(SystemConfigKeys.FOOTER_TEXT, configVO.getFooterText());
        saveOrUpdate(SystemConfigKeys.LOGO, configVO.getLogo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetToDefault() {
        for (Map.Entry<String, String> entry : DEFAULT_CONFIG.entrySet()) {
            saveOrUpdate(entry.getKey(), entry.getValue());
        }
    }

    // ========== 私有辅助方法 ==========

    private String getValue(Map<String, String> map, String key) {
        return map.getOrDefault(key, DEFAULT_CONFIG.get(key));
    }

    private Integer getIntValue(Map<String, String> map, String key) {
        String val = map.get(key);
        if (val == null) {
            val = DEFAULT_CONFIG.get(key);
        }
        try {
            return val != null ? Integer.parseInt(val) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean getBooleanValue(Map<String, String> map, String key) {
        String val = map.get(key);
        if (val == null) {
            val = DEFAULT_CONFIG.get(key);
        }
        return val != null ? Boolean.parseBoolean(val) : null;
    }

    public void saveOrUpdate(String key, String value) {
        LambdaQueryWrapper<SystemConfiguration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfiguration::getConfigKey, key);
        SystemConfiguration existing = configMapper.selectOne(wrapper);

        String finalValue = value != null ? value : "";

        if (existing != null) {
            existing.setConfigValue(finalValue);
            configMapper.updateById(existing);
        } else {
            SystemConfiguration config = new SystemConfiguration();
            config.setConfigKey(key);
            config.setConfigValue(finalValue);
            config.setDescription(key);
            config.setType("0");
            configMapper.insert(config);
        }
    }
}
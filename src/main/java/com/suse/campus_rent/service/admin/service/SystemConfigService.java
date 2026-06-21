package com.suse.campus_rent.service.admin.service;

import com.suse.campus_rent.vo.admin.SystemConfigVO;

public interface SystemConfigService {
    SystemConfigVO getConfig();

    void saveConfig(SystemConfigVO configVO);

    void resetToDefault();
}
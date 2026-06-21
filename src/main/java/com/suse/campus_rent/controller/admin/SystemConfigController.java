package com.suse.campus_rent.controller.admin;

import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.service.admin.service.SystemConfigService;
import com.suse.campus_rent.vo.admin.SystemConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    /**
     * 获取所有系统配置
     */
    @GetMapping
    public Result<SystemConfigVO> getConfig() {
        return Result.success(systemConfigService.getConfig());
    }

    /**
     * 保存系统配置
     */
    @OperLog(title = "保存系统配置", category = "SYSTEM", level = "INFO")
    @PostMapping
    public Result<?> saveConfig(@RequestBody SystemConfigVO configVO) {
        systemConfigService.saveConfig(configVO);
        return Result.success("配置保存成功");
    }

    /**
     * 重置为默认配置
     */
    @OperLog(title = "重置系统配置", category = "SYSTEM", level = "WARN")
    @PostMapping("/reset")
    public Result<?> resetConfig() {
        systemConfigService.resetToDefault();
        return Result.success("配置已重置为默认值");
    }
}

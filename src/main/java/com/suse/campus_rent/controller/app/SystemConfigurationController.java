package com.suse.campus_rent.controller.app;

import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.service.app.service.SystemConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * SystemConfigurationController
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/21 15:01
 */
@RestController
@RequestMapping("/sys")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SystemConfigurationController {

    private final SystemConfigurationService sys;

    @GetMapping
    public Result<?> initSys() {
        Map<String, String> init = sys.init();
        return Result.success(init);
    }
}

package com.suse.campus_rent.controller.app;

import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.service.app.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public Result<?> getServices() {
        return serviceService.list();
    }
}
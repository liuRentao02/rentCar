package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.entity.Services;
import com.suse.campus_rent.mapper.ServiceMapper;
import com.suse.campus_rent.service.app.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ServiceServiceImpl
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/8 20:41
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceServiceImpl implements ServiceService {

    private final ServiceMapper serviceMapper;

    @Override
    public Result<?> list() {
        QueryWrapper<Services> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<Services> services = serviceMapper.selectList(queryWrapper);
        if (services.isEmpty()) {
            return Result.error("没有服务");
        }
        return Result.success(services);
    }
}

package com.suse.campus_rent.service.app.service.impl;

import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.entity.RoleBenefits;
import com.suse.campus_rent.mapper.RoleBenefitsMapper;
import com.suse.campus_rent.service.app.service.RoleBenefitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RoleBenefitsServiceImpl
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:33
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleBenefitsServiceImpl implements RoleBenefitsService {

    private final RoleBenefitsMapper roleBenefitsMapper;

    @Override
    public Result<?> getBenefits() {
        List<RoleBenefits> roleBenefits = roleBenefitsMapper.selectList(null);
        if (roleBenefits.isEmpty()) {
            return Result.error("没有角色权益");
        }
        Map<String, RoleBenefits> map = new HashMap<>();
        for (RoleBenefits roleBenefit : roleBenefits) {
            if (roleBenefit.getRole().equals("admin")) {
                continue;
            }
            map.put(roleBenefit.getRole(), roleBenefit);
        }
        return Result.success(map);
    }
}

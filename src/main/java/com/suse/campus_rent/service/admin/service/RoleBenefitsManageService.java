package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.RoleBenefitsCreateDTO;
import com.suse.campus_rent.dto.admin.RoleBenefitsQueryDTO;
import com.suse.campus_rent.dto.admin.RoleBenefitsUpdateDTO;
import com.suse.campus_rent.vo.admin.RoleBenefitsVO;

public interface RoleBenefitsManageService {
    IPage<RoleBenefitsVO> listRoleBenefits(RoleBenefitsQueryDTO queryDTO);

    RoleBenefitsVO getRoleBenefit(Long id);

    void createRoleBenefit(RoleBenefitsCreateDTO createDTO);

    void updateRoleBenefit(RoleBenefitsUpdateDTO updateDTO);

    void deleteRoleBenefit(Long id);
}
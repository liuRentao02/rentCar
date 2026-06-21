package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.RoleBenefitsCreateDTO;
import com.suse.campus_rent.dto.admin.RoleBenefitsQueryDTO;
import com.suse.campus_rent.dto.admin.RoleBenefitsUpdateDTO;
import com.suse.campus_rent.entity.RoleBenefits;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.RoleBenefitsMapper;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.service.admin.service.RoleBenefitsManageService;
import com.suse.campus_rent.vo.admin.RoleBenefitsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service("adminRoleBenefitsManageService")
@RequiredArgsConstructor
public class RoleBenefitsManageServiceImpl implements RoleBenefitsManageService {

    private final RoleBenefitsMapper roleBenefitsMapper;
    private final UserMapper userMapper;

    @Override
    public IPage<RoleBenefitsVO> listRoleBenefits(RoleBenefitsQueryDTO queryDTO) {
        Page<RoleBenefits> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<RoleBenefits> rolePage = roleBenefitsMapper.selectRoleBenefitsPage(page, queryDTO);
        IPage<RoleBenefitsVO> voPage = new Page<>(rolePage.getCurrent(), rolePage.getSize(), rolePage.getTotal());
        voPage.setRecords(rolePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public RoleBenefitsVO getRoleBenefit(Long id) {
        RoleBenefits benefit = roleBenefitsMapper.selectById(id);
        if (benefit == null) {
            throw new BusinessException("权益配置不存在");
        }
        return convertToVO(benefit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRoleBenefit(RoleBenefitsCreateDTO createDTO) {
        // 检查角色标识是否已存在
        LambdaQueryWrapper<RoleBenefits> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleBenefits::getRole, createDTO.getRole());
        if (roleBenefitsMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色标识已存在");
        }

        RoleBenefits benefit = new RoleBenefits();
        BeanUtils.copyProperties(createDTO, benefit);
        roleBenefitsMapper.insert(benefit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleBenefit(RoleBenefitsUpdateDTO updateDTO) {
        RoleBenefits existing = roleBenefitsMapper.selectById(updateDTO.getId());
        if (existing == null) {
            throw new BusinessException("权益配置不存在");
        }

        // 如果修改了角色标识，检查新标识是否已被其他记录使用
        if (StringUtils.hasText(updateDTO.getRole()) && !updateDTO.getRole().equals(existing.getRole())) {
            LambdaQueryWrapper<RoleBenefits> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RoleBenefits::getRole, updateDTO.getRole());
            if (roleBenefitsMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("角色标识已存在");
            }
        }

        // 只更新非空字段
        if (StringUtils.hasText(updateDTO.getRole())) {
            existing.setRole(updateDTO.getRole());
        }
        if (updateDTO.getDepositRate() != null) {
            existing.setDepositRate(updateDTO.getDepositRate());
        }
        if (updateDTO.getRentDiscount() != null) {
            existing.setRentDiscount(updateDTO.getRentDiscount());
        }
        if (updateDTO.getFreeExtensionCount() != null) {
            existing.setFreeExtensionCount(updateDTO.getFreeExtensionCount());
        }
        if (updateDTO.getOverdueFeeRate() != null) {
            existing.setOverdueFeeRate(updateDTO.getOverdueFeeRate());
        }
        if (updateDTO.getDescription() != null) {
            existing.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPointsThreshold() != null) { // 新增：积分阈值
            existing.setPointsThreshold(updateDTO.getPointsThreshold());
        }

        roleBenefitsMapper.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleBenefit(Long id) {
        RoleBenefits existing = roleBenefitsMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("权益配置不存在");
        }

        // 检查是否有用户使用该角色
        Long userCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getRole, existing.getRole()));
        if (userCount > 0) {
            throw new BusinessException("该角色下存在用户，无法删除");
        }

        roleBenefitsMapper.deleteById(id);
    }

    private RoleBenefitsVO convertToVO(RoleBenefits entity) {
        RoleBenefitsVO vo = new RoleBenefitsVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
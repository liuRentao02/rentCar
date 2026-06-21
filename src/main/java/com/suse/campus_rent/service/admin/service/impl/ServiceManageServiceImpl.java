package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.ServiceCreateDTO;
import com.suse.campus_rent.dto.admin.ServiceQueryDTO;
import com.suse.campus_rent.dto.admin.ServiceUpdateDTO;
import com.suse.campus_rent.entity.Services;
import com.suse.campus_rent.mapper.ServiceMapper;
import com.suse.campus_rent.service.admin.service.ServiceManageService;
import com.suse.campus_rent.vo.admin.ServiceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service("adminServiceManageService")
@RequiredArgsConstructor
public class ServiceManageServiceImpl implements ServiceManageService {

    private final ServiceMapper serviceMapper;

    @Override
    public IPage<ServiceVO> listServices(ServiceQueryDTO queryDTO) {
        Page<Services> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<Services> servicePage = serviceMapper.selectServicePage(page, queryDTO);
        IPage<ServiceVO> voPage = new Page<>(servicePage.getCurrent(), servicePage.getSize(), servicePage.getTotal());
        voPage.setRecords(servicePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public ServiceVO getService(Long id) {
        Services service = serviceMapper.selectById(id);
        if (service == null) {
            throw new BusinessException("服务不存在");
        }
        return convertToVO(service);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createService(ServiceCreateDTO createDTO) {
        checkNameUnique(createDTO.getName(), null);
        Services service = new Services();
        BeanUtils.copyProperties(createDTO, service);
        serviceMapper.insert(service);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateService(ServiceUpdateDTO updateDTO) {
        Services existing = serviceMapper.selectById(updateDTO.getId());
        if (existing == null) {
            throw new BusinessException("服务不存在");
        }

        if (StringUtils.hasText(updateDTO.getName()) && !updateDTO.getName().equals(existing.getName())) {
            checkNameUnique(updateDTO.getName(), updateDTO.getId());
        }

        // 只更新非空字段
        if (StringUtils.hasText(updateDTO.getName())) {
            existing.setName(updateDTO.getName());
        }
        if (StringUtils.hasText(updateDTO.getDescription())) {
            existing.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPrice() != null) {
            existing.setPrice(updateDTO.getPrice());
        }
        if (StringUtils.hasText(updateDTO.getType())) {
            existing.setType(updateDTO.getType());
        }
        if (updateDTO.getStatus() != null) {
            existing.setStatus(updateDTO.getStatus());
        }

        serviceMapper.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteService(Long id) {
        Services existing = serviceMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("服务不存在");
        }
        serviceMapper.deleteById(id); // 物理删除，或改为逻辑删除
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Services service = serviceMapper.selectById(id);
        if (service == null) {
            throw new BusinessException("服务不存在");
        }
        service.setStatus(status);
        serviceMapper.updateById(service);
    }

    // ========== 私有方法 ==========

    private ServiceVO convertToVO(Services entity) {
        ServiceVO vo = new ServiceVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    private void checkNameUnique(String name, Long excludeId) {
        LambdaQueryWrapper<Services> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Services::getName, name);
        if (excludeId != null) {
            wrapper.ne(Services::getId, excludeId);
        }
        if (serviceMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("服务名称已存在");
        }
    }
}
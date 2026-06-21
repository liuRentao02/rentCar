package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.ServiceCreateDTO;
import com.suse.campus_rent.dto.admin.ServiceQueryDTO;
import com.suse.campus_rent.dto.admin.ServiceUpdateDTO;
import com.suse.campus_rent.vo.admin.ServiceVO;

public interface ServiceManageService {
    IPage<ServiceVO> listServices(ServiceQueryDTO queryDTO);

    ServiceVO getService(Long id);

    void createService(ServiceCreateDTO createDTO);

    void updateService(ServiceUpdateDTO updateDTO);

    void deleteService(Long id);

    void updateStatus(Long id, Integer status);
}
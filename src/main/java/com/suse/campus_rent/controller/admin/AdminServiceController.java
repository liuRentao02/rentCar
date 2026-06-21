package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.ServiceCreateDTO;
import com.suse.campus_rent.dto.admin.ServiceQueryDTO;
import com.suse.campus_rent.dto.admin.ServiceUpdateDTO;
import com.suse.campus_rent.service.admin.service.ServiceManageService;
import com.suse.campus_rent.vo.admin.ServiceVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class AdminServiceController {

    private final ServiceManageService serviceManageService;

    @GetMapping
    public Result<IPage<ServiceVO>> listServices(ServiceQueryDTO queryDTO) {
        return Result.success(serviceManageService.listServices(queryDTO));
    }

    @GetMapping("/{id}")
    public Result<ServiceVO> getService(@PathVariable Long id) {
        return Result.success(serviceManageService.getService(id));
    }

    @OperLog(title = "新增增值服务", category = "SERVICE", level = "INFO")
    @PostMapping
    public Result<?> createService(@Valid @RequestBody ServiceCreateDTO createDTO) {
        serviceManageService.createService(createDTO);
        return Result.success("新增成功");
    }

    @OperLog(title = "修改增值服务", category = "SERVICE", level = "INFO")
    @PutMapping("/{id}")
    public Result<?> updateService(@PathVariable Long id, @Valid @RequestBody ServiceUpdateDTO updateDTO) {
        updateDTO.setId(id);
        serviceManageService.updateService(updateDTO);
        return Result.success("更新成功");
    }

    @OperLog(title = "删除增值服务", category = "SERVICE", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteService(@PathVariable Long id) {
        serviceManageService.deleteService(id);
        return Result.success("删除成功");
    }

    @OperLog(title = "修改服务状态", category = "SERVICE", level = "INFO")
    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        serviceManageService.updateStatus(id, status);
        return Result.success("状态更新成功");
    }
}

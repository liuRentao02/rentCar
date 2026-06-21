package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.RoleBenefitsCreateDTO;
import com.suse.campus_rent.dto.admin.RoleBenefitsQueryDTO;
import com.suse.campus_rent.dto.admin.RoleBenefitsUpdateDTO;
import com.suse.campus_rent.service.admin.service.RoleBenefitsManageService;
import com.suse.campus_rent.vo.admin.RoleBenefitsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/role-benefits")
@RequiredArgsConstructor
public class AdminRoleBenefitsController {

    private final RoleBenefitsManageService roleBenefitsManageService;

    @GetMapping
    public Result<IPage<RoleBenefitsVO>> listRoleBenefits(RoleBenefitsQueryDTO queryDTO) {
        return Result.success(roleBenefitsManageService.listRoleBenefits(queryDTO));
    }

    @GetMapping("/{id}")
    public Result<RoleBenefitsVO> getRoleBenefit(@PathVariable Long id) {
        return Result.success(roleBenefitsManageService.getRoleBenefit(id));
    }

    @OperLog(title = "新增角色权益", category = "ROLE", level = "INFO")
    @PostMapping
    public Result<?> createRoleBenefit(@Valid @RequestBody RoleBenefitsCreateDTO createDTO) {
        roleBenefitsManageService.createRoleBenefit(createDTO);
        return Result.success("新增成功");
    }

    @OperLog(title = "修改角色权益", category = "ROLE", level = "INFO")
    @PutMapping("/{id}")
    public Result<?> updateRoleBenefit(@PathVariable Long id, @Valid @RequestBody RoleBenefitsUpdateDTO updateDTO) {
        updateDTO.setId(id);
        roleBenefitsManageService.updateRoleBenefit(updateDTO);
        return Result.success("更新成功");
    }

    @OperLog(title = "删除角色权益", category = "ROLE", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteRoleBenefit(@PathVariable Long id) {
        roleBenefitsManageService.deleteRoleBenefit(id);
        return Result.success("删除成功");
    }
}

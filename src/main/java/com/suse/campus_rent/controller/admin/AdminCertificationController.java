package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.AdminCreateDTO;
import com.suse.campus_rent.dto.app.CertificationAuditDTO;
import com.suse.campus_rent.entity.UserCertification;
import com.suse.campus_rent.service.app.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * AdminCertificationController
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/17 21:58
 */
@Slf4j
@RestController
@RequestMapping("/admin/certification")
@RequiredArgsConstructor
public class AdminCertificationController {
    private final CertificationService certificationService;
    // ==================== 管理员接口 ====================

    @GetMapping("/list")
    public Result<IPage<UserCertification>> getCertificationList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        return Result.success(certificationService.getCertificationList(page, size, status));
    }

    @OperLog(title = "审核实名认证", category = "USER", level = "INFO")
    @PutMapping("/audit")
    public Result<?> auditCertification(@RequestBody @Valid CertificationAuditDTO dto,
                                        @RequestParam Long adminUserId) {
        certificationService.auditCertification(dto, adminUserId);
        return Result.success("审核完成");
    }

    @OperLog(title = "新增用户", category = "USER", level = "INFO")
    @PostMapping("/add")
    public Result<?> addUser(@RequestBody AdminCreateDTO add) {
        log.info(add.toString());
        certificationService.add(add);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteCertification(@PathVariable Long id) {
        certificationService.delete(id);
        return Result.success();
    }
}

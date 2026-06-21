package com.suse.campus_rent.controller.app;

import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.app.CertificationApplyDTO;
import com.suse.campus_rent.entity.UserCertification;
import com.suse.campus_rent.service.app.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certification")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    // ==================== 用户端接口 ====================

    @OperLog(title = "申请学生认证", category = "CERT", level = "INFO")
    @PostMapping("/student/apply")
    public Result<?> applyStudentCertification(@RequestBody @Valid CertificationApplyDTO dto) {
        certificationService.applyStudentCertification(dto);
        return Result.success("申请提交成功，请等待审核");
    }
}

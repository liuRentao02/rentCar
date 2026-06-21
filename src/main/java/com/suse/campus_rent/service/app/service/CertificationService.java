package com.suse.campus_rent.service.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.AdminCreateDTO;
import com.suse.campus_rent.dto.app.CertificationApplyDTO;
import com.suse.campus_rent.dto.app.CertificationAuditDTO;
import com.suse.campus_rent.entity.UserCertification;

public interface CertificationService {
    // 学生认证申请
    void applyStudentCertification(CertificationApplyDTO dto);

    // 分页查询待审核/已审核列表（管理员）
    IPage<UserCertification> getCertificationList(Integer page, Integer size, Integer status);

    // 审核认证
    void auditCertification(CertificationAuditDTO dto, Long adminUserId);

    void add(AdminCreateDTO add);

    void delete(Long id);
}
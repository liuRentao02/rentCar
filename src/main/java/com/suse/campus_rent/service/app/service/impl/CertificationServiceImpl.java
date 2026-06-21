package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.AdminCreateDTO;
import com.suse.campus_rent.dto.app.CertificationApplyDTO;
import com.suse.campus_rent.dto.app.CertificationAuditDTO;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.entity.UserCertification;
import com.suse.campus_rent.mapper.UserCertificationMapper;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.service.app.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {

    private final UserCertificationMapper certificationMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void applyStudentCertification(CertificationApplyDTO dto) {
        // 检查是否已有待审核或已通过的学生认证
        QueryWrapper<UserCertification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", dto.getUserId())
                .eq("cert_type", "student")
                .in("status", 0, 1);
        if (certificationMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已有进行中的学生认证申请或已认证通过");
        }

        UserCertification cert = new UserCertification();
        cert.setUserId(dto.getUserId());
        cert.setCertType("student");
        cert.setRealName(dto.getRealName());
        cert.setStudentId(dto.getStudentId());
        cert.setSchool(dto.getSchool());
        cert.setIdCard(dto.getIdCard());
        cert.setImageUrls(dto.getImageUrls());
        cert.setStatus(0); // 待审核
        cert.setApplyTime(LocalDateTime.now());
        certificationMapper.insert(cert);
    }

    @Override
    public IPage<UserCertification> getCertificationList(Integer page, Integer size, Integer status) {
        Page<UserCertification> pageParam = new Page<>(page, size);
        QueryWrapper<UserCertification> wrapper = new QueryWrapper<>();
        if (status != null && status >= 0) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("apply_time");
        return certificationMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional
    public void auditCertification(CertificationAuditDTO dto, Long adminUserId) {
        UserCertification cert = certificationMapper.selectById(dto.getId());
        if (cert == null) {
            throw new BusinessException("认证记录不存在");
        }
        if (cert.getStatus() != 0) {
            throw new BusinessException("该记录已审核，请勿重复操作");
        }

        cert.setStatus(dto.getStatus());
        cert.setAuditRemark(dto.getRemark());
        cert.setAuditTime(LocalDateTime.now());
        cert.setAuditUserId(adminUserId);
        certificationMapper.updateById(cert);

        // 如果审核通过，更新用户角色为 student
        if (dto.getStatus() == 1) {
            User user = userMapper.selectById(cert.getUserId());
            if (user != null && !"student".equals(user.getRole())) {
                user.setRole("student");
                user.setStudentExpireTime(LocalDate.now().plusYears(4));
                userMapper.updateById(user);
            }
        }
    }

    @Override
    public void add(AdminCreateDTO add) {
        if (add.getUserId() == null) {
            throw new BusinessException("用户id为空");
        }
        User user = userMapper.selectById(add.getUserId());
        if (user == null) {
            throw new BusinessException("查无此人");
        }
        if (user.getRole().equals("student")) {
            throw new BusinessException("已经是学生了");
        }
        UserCertification cert = new UserCertification();
        cert.setStatus(1);
        cert.setAuditUserId(add.getAuditUserId());
        cert.setSchool(add.getSchool());
        cert.setUserId(add.getUserId());
        cert.setRealName(add.getRealName());
        cert.setStudentId(add.getStudentId());
        cert.setImageUrls(add.getImageUrls());
        cert.setApplyTime(LocalDateTime.now());
        cert.setAuditTime(LocalDateTime.now());
        cert.setAuditRemark("管理员添加");
        cert.setCertType("student");
        certificationMapper.insert(cert);

        // 如果审核通过，更新用户角色为 student
        if (add.getStatus() == 1) {
            User users = userMapper.selectById(cert.getUserId());
            if (users != null && !"student".equals(users.getRole())) {
                users.setRole("student");
                users.setStudentExpireTime(LocalDate.now().plusYears(4));
                userMapper.updateById(users);
            }
        }
    }

    @Override
    public void delete(Long id) {
        UserCertification userCertification = certificationMapper.selectById(id);
        if (userCertification != null) {
            User user = userMapper.selectById(userCertification.getUserId());
            user.setRole("user");
            user.setStudentExpireTime(null);
            userMapper.updateById(user);
        }
        certificationMapper.deleteById(id);
    }
}
package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.ChangePasswordDTO;
import com.suse.campus_rent.dto.admin.ProfileUpdateDTO;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.service.admin.service.UserService;
import com.suse.campus_rent.service.admin.service.UserCenterService;
import com.suse.campus_rent.vo.admin.UserProfileVO;
import com.suse.campus_rent.vo.admin.UserStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserCenterController {

    private final UserCenterService userCenterService;
    private final UserMapper userMapper; // 用于获取userId
    private final UserService adminUserService;

    private Long getCurrentUserId() {
        String username = getCurrentUsername();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        return user != null ? user.getId() : null;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * 获取当前用户个人信息
     */
    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile() {
        Long userId = getCurrentUserId();
        return Result.success(userCenterService.getCurrentUserProfile(userId));
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/stats")
    public Result<UserStatsVO> getStats() {
        Long userId = getCurrentUserId();
        return Result.success(userCenterService.getUserStats(userId));
    }

    /**
     * 更新个人资料
     */
    @OperLog(title = "更新个人资料", category = "USER", level = "INFO")
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody ProfileUpdateDTO dto) {
        userCenterService.updateProfile(dto.getId(), dto);
        return Result.success("资料更新成功");
    }

    /**
     * 修改密码
     */
    @OperLog(title = "修改密码", category = "USER", level = "WARN")
    @PostMapping("/change-password")
    public Result<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        userCenterService.changePassword(dto.getId(), dto);
        return Result.success("密码修改成功");
    }

    /**
     * 上传头像
     */
    @OperLog(title = "上传头像", category = "USER", level = "INFO")
    @PostMapping("/avatar/{userId}")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long userId) {
        String url;
        try {
            url = userCenterService.uploadAvatar(userId, file);
        } catch (IOException e) {
            throw new BusinessException("图片上传失败");
        }
        return Result.success(url);
    }
}

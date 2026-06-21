package com.suse.campus_rent.controller.app;

import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.app.UpdatePasswordDTO;
import com.suse.campus_rent.dto.app.UserUpdateDTO;
import com.suse.campus_rent.service.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/8 21:33
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    // 查询用户信息 - 不加日志
    @GetMapping
    public Result<?> getUserInfo(@RequestParam("id") Long id) {
        return userService.getUserInfoById(id);
    }

    // 查询个人资料 - 不加日志
    @GetMapping("/profile")
    public Result<?> getProfile(@RequestParam("id") Long id) {
        return userService.getProfile(id);
    }

    @OperLog(title = "更新个人资料", category = "USER", level = "INFO")
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody @Valid UserUpdateDTO dto) {
        return userService.updateProfile(dto);
    }

    @OperLog(title = "修改密码", category = "USER", level = "INFO")
    @PutMapping("/password")
    public Result<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto) {
        userService.updatePassword(dto.getUserId(), dto.getOldPassword(), dto.getNewPassword());
        return Result.success("密码修改成功");
    }
}
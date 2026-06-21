package com.suse.campus_rent.service.app.service;

import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.app.ForgetUser;
import com.suse.campus_rent.dto.app.LoginUser;
import com.suse.campus_rent.dto.app.RegisterUser;
import com.suse.campus_rent.dto.app.UserUpdateDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * UserService
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:31
 */
public interface UserService {
    Result<?> login(LoginUser loginUser, HttpServletRequest request);

    Result<?> register(RegisterUser registerUser);

    Result<?> getUserInfoById(Long id);

    Result<?> getProfile(Long id);

    Result<?> updateProfile(@Valid UserUpdateDTO dto);

    Object Forget(ForgetUser user);

    Result<?> updatePassword(Long id, String oldPassword, String newPassword);
}

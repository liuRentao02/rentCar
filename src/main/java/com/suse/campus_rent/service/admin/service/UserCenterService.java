package com.suse.campus_rent.service.admin.service;


import com.suse.campus_rent.dto.admin.ChangePasswordDTO;
import com.suse.campus_rent.dto.admin.ProfileUpdateDTO;
import com.suse.campus_rent.vo.admin.UserProfileVO;
import com.suse.campus_rent.vo.admin.UserStatsVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserCenterService {
    UserProfileVO getCurrentUserProfile(Long userId);

    UserStatsVO getUserStats(Long userId);

    void updateProfile(Long userId, ProfileUpdateDTO dto);

    void changePassword(Long userId, ChangePasswordDTO dto);

    String uploadAvatar(Long userId, MultipartFile file) throws IOException;
}
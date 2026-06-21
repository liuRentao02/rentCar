package com.suse.campus_rent.controller.app;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.common.SystemConfigKeys;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.service.admin.service.impl.SystemConfigServiceImpl;
import com.suse.campus_rent.util.FileUploadUtil;
import com.suse.campus_rent.vo.app.AttachmentUploadVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UploadController {

    private final FileUploadUtil fileUploadUtil;
    private final SystemConfigServiceImpl sys;
    private final UserMapper userMapper;

    @OperLog(title = "上传头像", category = "UPLOAD", level = "INFO")
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = fileUploadUtil.uploadImage(file);
            log.info("头像上传成功: {}", imageUrl);
            return Result.success(imageUrl);
        } catch (IOException e) {
            log.error("头像上传失败", e);
            return Result.error("头像上传失败：" + e.getMessage());
        } catch (RuntimeException e) {
            log.error("头像上传失败", e);
            return Result.error(e.getMessage());
        }
    }

    @OperLog(title = "上传并更新用户头像", category = "UPLOAD", level = "INFO")
    @PostMapping("/avatar/{id}")
    public Result<String> uploadAvatars(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        String u = fileUploadUtil.uploadImage(file);
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.eq(User::getId, id);
        qw.set(User::getAvatar, u);
        userMapper.update(qw);
        return Result.success(u);
    }

    @OperLog(title = "上传Logo", category = "UPLOAD", level = "INFO")
    @PostMapping("/logo")
    public Result<String> uploadLogo(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileUploadUtil.uploadImage(file);
            sys.saveOrUpdate(SystemConfigKeys.LOGO, url);
            return Result.success();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @OperLog(title = "上传通知附件", category = "UPLOAD", level = "INFO")
    @PostMapping("/notice")
    public Result<AttachmentUploadVO> uploadNoticeAttachment(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(fileUploadUtil.uploadNotice(file));
    }

    @OperLog(title = "删除通知附件", category = "UPLOAD", level = "WARN")
    @DeleteMapping("/notice/{id}")
    public Result<?> deleteNoticeAttachment(@PathVariable Long id) {
        return fileUploadUtil.deleteNoticeAttachment(id);
    }

    @OperLog(title = "上传图片(RImg)", category = "UPLOAD", level = "INFO")
    @PostMapping("/RImg")
    public Result<?> uploadRImg(@RequestParam("file") MultipartFile file) throws IOException {
        String s = fileUploadUtil.uploadImage(file);
        return Result.success(s);
    }

    @OperLog(title = "上传图片(image)", category = "UPLOAD", level = "INFO")
    @PostMapping("/image")
    public Result<?> uploadRImage(@RequestParam("file") MultipartFile file) throws IOException {
        String s = fileUploadUtil.uploadImage(file);
        return Result.success(s);
    }

    @OperLog(title = "删除图片", category = "UPLOAD", level = "WARN")
    @DeleteMapping("/delete")
    public Result<?> deleteRImg(@RequestParam("url") String url) {
        fileUploadUtil.deleteImage(url);
        return Result.success();
    }
}
package com.suse.campus_rent.util;

import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.entity.NoticeAttachment;
import com.suse.campus_rent.mapper.NoticeAttachmentMapper;
import com.suse.campus_rent.vo.app.AttachmentUploadVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadUtil {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.path}")
    private String accessPath;

    private final NoticeAttachmentMapper attachmentMapper;

    /**
     * 上传图片并返回访问路径
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // 1. 检查文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 2. 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("只能上传图片文件");
        }

        // 3. 检查文件大小 (限制2MB)
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new BusinessException("图片大小不能超过2MB");
        }

        // 4. 创建目标目录
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String targetDirPath = uploadPath + datePath;
        File targetDir = new File(targetDirPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        // 5. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;

        // 6. 保存文件
        String filePath = targetDirPath + "/" + filename;
        File destFile = new File(filePath);
        file.transferTo(destFile);

        // 7. 返回访问路径
        return "http://localhost:8080/api" + accessPath + datePath + "/" + filename;
    }

    /**
     * 根据访问路径删除图片
     *
     * @param imagePath 访问路径，例如: /uploads/2026/02/06/f6e72de6-2b3b-4285-a9df-a73f1d1e5cbb.jpg
     * @return true-删除成功, false-删除失败
     */
    public boolean deleteImage(String imagePath) {
        try {
            if (imagePath == null) {
                return false;
            }
            if (imagePath.startsWith("http")) {
                imagePath = imagePath.replace("http://localhost:8080/api", "");
            }
            // 1. 安全检查：防止路径遍历攻击
            if (!imagePath.startsWith(accessPath)) {
                return false;
            }

            // 2. 去掉访问路径前缀，获取相对路径
            // 例如: /uploads/2026/02/06/xxx.jpg -> 2026/02/06/xxx.jpg
            String relativePath = imagePath.substring(accessPath.length());

            // 3. 构建服务器上的物理路径
            // uploadPath: D:/campus-rental/uploads/
            // relativePath: 2026/02/06/xxx.jpg
            // 完整路径: D:/campus-rental/uploads/2026/02/06/xxx.jpg
            String fullPath = uploadPath + relativePath;

            // 4. 删除文件
            File file = new File(fullPath);

            if (file.exists()) {
                boolean deleted = file.delete();
                log.info("删除图片: {}", fullPath);
                return deleted;
            } else {
                log.error("图片不存在: {}", fullPath);
                return false;
            }
        } catch (Exception e) {
            log.error("删除图片失败: {}", e.getMessage());
            return false;
        }
    }

    public AttachmentUploadVO uploadNotice(MultipartFile file) throws IOException {
        // 1. 保存文件到服务器
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + suffix;
        File dest = new File(uploadPath, newFileName);
        file.transferTo(dest);

        // 2. 保存附件记录到数据库
        NoticeAttachment attachment = new NoticeAttachment();
        attachment.setFileName(originalFilename);
        attachment.setFileUrl("http://localhost:8080/api" + accessPath + newFileName);
        attachment.setFileSize(file.getSize());
        attachment.setCreateTime(LocalDateTime.now());
        attachmentMapper.insert(attachment);

        // 3. 返回响应
        AttachmentUploadVO resp = new AttachmentUploadVO();
        resp.setId(attachment.getId());
        resp.setName(originalFilename);
        resp.setUrl(attachment.getFileUrl());
        return resp;
    }

    public Result<?> deleteNoticeAttachment(Long attachmentId) {
        if (attachmentId == null) {
            log.warn("附件ID为空，删除失败");
            return Result.error("附件ID不能为空");
        }

        NoticeAttachment attachment = attachmentMapper.selectById(attachmentId);
        if (attachment == null) {
            log.warn("附件不存在，ID: {}", attachmentId);
            return Result.error("附件不存在");
        }

        // 物理删除文件（可选）
        try {
            String filePath = uploadPath + "/" + extractFileName(attachment.getFileUrl());
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                log.info("删除附件文件: {}，结果: {}", filePath, deleted);
            } else {
                log.warn("附件文件不存在，路径: {}", filePath);
            }
        } catch (Exception e) {
            log.warn("删除附件文件失败，ID: {}", attachmentId, e);
        }

        // 删除数据库记录
        int deleteCount = attachmentMapper.deleteById(attachmentId);
        log.info("删除附件数据库记录，ID: {}，结果: {}", attachmentId, deleteCount > 0);

        return Result.success("附件已删除");
    }

    private String extractFileName(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    /**
     * 上传 Base64 编码的图片，返回访问路径
     *
     * @param base64Data Base64 数据，可带前缀 "data:image/png;base64,"
     * @return 图片访问路径
     * @throws IOException       文件写入异常
     * @throws BusinessException 数据为空、格式错误、非图片或超过大小限制
     */
    public String uploadBase64Image(String base64Data) throws IOException {
        // 1. 空值检查
        if (base64Data == null || base64Data.isEmpty()) {
            throw new BusinessException("Base64 数据不能为空");
        }

        // 2. 解析 data URL 前缀，提取 MIME 类型和纯 Base64 内容
        String base64 = base64Data;
        String mimeType = null;
        if (base64Data.startsWith("data:")) {
            int commaIndex = base64Data.indexOf(",");
            if (commaIndex < 0) {
                throw new BusinessException("Base64 格式错误，缺少逗号分隔符");
            }
            String prefix = base64Data.substring(0, commaIndex);
            base64 = base64Data.substring(commaIndex + 1);
            // 从 "data:image/jpeg;base64" 中提取 MIME 类型
            if (prefix.contains(":")) {
                String[] parts = prefix.split(":");
                if (parts.length >= 2) {
                    mimeType = parts[1].split(";")[0];
                }
            }
        }

        // 3. 校验是否为图片类型（依据 MIME）
        if (mimeType != null && !mimeType.startsWith("image/")) {
            throw new BusinessException("只能上传图片文件");
        }

        // 4. Base64 解码
        byte[] imageBytes;
        try {
            imageBytes = Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Base64 解码失败，请检查数据格式");
        }

        // 5. 文件大小限制（与 uploadImage 保持一致：2MB）
        if (imageBytes.length > 2 * 1024 * 1024) {
            throw new BusinessException("图片大小不能超过2MB");
        }

        // 6. 根据 MIME 类型确定文件扩展名，默认为 .jpg
        String extension;
        if (mimeType != null) {
            extension = switch (mimeType) {
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                case "image/gif" -> ".gif";
                case "image/webp" -> ".webp";
                case "image/bmp" -> ".bmp";
                default -> ".jpg";
            };
        } else {
            extension = ".jpg";
        }

        // 7. 创建日期分类目录（与 uploadImage 路径结构一致）
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String targetDirPath = uploadPath + datePath;
        File targetDir = new File(targetDirPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        // 8. 生成唯一文件名并写入文件
        String filename = UUID.randomUUID().toString() + extension;
        String filePath = targetDirPath + "/" + filename;
        Files.write(Paths.get(filePath), imageBytes);

        // 9. 返回可访问的 URL
        return "http://localhost:8080/api" + accessPath + datePath + "/" + filename;
    }
}

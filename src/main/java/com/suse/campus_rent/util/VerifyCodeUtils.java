package com.suse.campus_rent.util;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码生成工具类
 */
public class VerifyCodeUtils {

    // 验证码字符集
    private static final String VERIFY_CODES = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 生成随机验证码
     *
     * @param verifySize 验证码长度
     * @return 验证码字符串
     */
    public static String generateVerifyCode(int verifySize) {
        Random random = new Random();
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(VERIFY_CODES.charAt(random.nextInt(VERIFY_CODES.length())));
        }
        return verifyCode.toString();
    }

    /**
     * 生成验证码图片的Base64编码和验证码文本
     *
     * @param width      图片宽度
     * @param height     图片高度
     * @param verifySize 验证码长度
     * @return 包含Base64编码和验证码文本的Result对象
     */
    public static VerifyCodeResult generate(int width, int height, int verifySize) {
        String code = generateVerifyCode(verifySize);
        BufferedImage image = createImage(width, height, code);
        String base64Image = imageToBase64(image);
        return new VerifyCodeResult(code, base64Image);
    }

    /**
     * 创建验证码图片
     *
     * @param width  图片宽度
     * @param height 图片高度
     * @param code   验证码文本
     * @return BufferedImage对象
     */
    private static BufferedImage createImage(int width, int height, String code) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        Random random = new Random();

        // 设置背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, height - 4));

        // 绘制干扰线
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.setColor(getRandColor(160, 200));
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 绘制验证码
        int temp = width / code.length();
        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            int x = i * temp + random.nextInt(1, 10);
            g.drawString(String.valueOf(code.charAt(i)), x, height + random.nextInt(-5, 2));
        }

        g.dispose();
        return image;
    }

    /**
     * 获取随机颜色
     *
     * @param fc 前景色范围最小值
     * @param bc 背景色范围最大值
     * @return 随机颜色对象
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 将BufferedImage转换为Base64字符串
     *
     * @param image BufferedImage对象
     * @return Base64编码的字符串
     */
    private static String imageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 将BufferedImage写入ByteArrayOutputStream
            ImageIO.write(image, "png", outputStream);
            // 将字节数组转换为Base64字符串
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证码结果类，用于封装验证码文本和Base64图片
     */
    @Data
    public static class VerifyCodeResult {
        private String code; // 验证码文本
        private String image; // 验证码图片的Base64编码

        public VerifyCodeResult(String code, String image) {
            this.code = code;
            this.image = image;
        }
    }
}

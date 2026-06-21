package com.suse.www.campus_rent;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 把你原本的密码放这里，比如 "123456"
        String rawPassword = "123456";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("加密后的密码: " + encodedPassword);
        // 输出示例：$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH
    }
}

package com.suse.campus_rent.service.app.service.impl;

import com.suse.campus_rent.dto.app.ContactSubmitDTO;
import com.suse.campus_rent.service.app.service.ContactService;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.entity.ContactMessage;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.ContactMessageMapper;
import com.suse.campus_rent.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactMessageMapper contactMessageMapper;
    private final UserMapper userMapper;

    @Override
    public void submitMessage(ContactSubmitDTO dto) {
        // 校验用户是否存在
        User user = userMapper.selectById(dto.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        ContactMessage message = new ContactMessage();
        message.setUserId(dto.getUserId());
        // 主题默认为“其他”
        String subject = StringUtils.hasText(dto.getSubject()) ? dto.getSubject() : "其他";
        message.setSubject(subject);
        message.setMessage(dto.getMessage());
        contactMessageMapper.insert(message);
    }
}
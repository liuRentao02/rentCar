package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.service.admin.service.ContactMessageService;
import com.suse.campus_rent.vo.admin.ContactMessageVO;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.entity.ContactMessage;
import com.suse.campus_rent.mapper.ContactMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminContactMessageService")
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageMapper contactMessageMapper;

    @Override
    public IPage<ContactMessageVO> listMessages(Integer page, Integer size, String keyword) {
        Page<ContactMessageVO> pageParam = new Page<>(page, size);
        return contactMessageMapper.selectPageWithUser(pageParam, keyword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long id) {
        ContactMessage message = contactMessageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }

        contactMessageMapper.deleteById(message.getId());
    }
}
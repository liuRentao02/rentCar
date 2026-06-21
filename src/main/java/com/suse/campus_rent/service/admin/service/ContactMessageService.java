package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.vo.admin.ContactMessageVO;

public interface ContactMessageService {
    IPage<ContactMessageVO> listMessages(Integer page, Integer size, String keyword);

    void deleteMessage(Long id);
}
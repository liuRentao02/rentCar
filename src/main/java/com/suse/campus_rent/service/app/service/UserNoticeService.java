package com.suse.campus_rent.service.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.app.AppNoticeQueryDTO;
import com.suse.campus_rent.entity.Notice;
import com.suse.campus_rent.vo.app.NoticeDetailVO;

public interface UserNoticeService {
    IPage<Notice> listPublishedNotices(AppNoticeQueryDTO queryDTO);

    NoticeDetailVO getNoticeDetail(Long id);

    String putReadNotice(Long id, Long userId);

    String deleteNotice(Long id, Long userId);
}
package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.NoticeCreateDTO;
import com.suse.campus_rent.dto.admin.NoticeQueryDTO;
import com.suse.campus_rent.dto.admin.NoticeUpdateDTO;
import com.suse.campus_rent.vo.admin.NoticeDetailVO;
import com.suse.campus_rent.vo.admin.NoticeListVO;
import com.suse.campus_rent.vo.admin.NoticeStatisticsVO;

public interface NoticeService {
    NoticeStatisticsVO getStatistics();

    IPage<NoticeListVO> listNotices(NoticeQueryDTO queryDTO);

    NoticeDetailVO getNoticeDetail(Long id);

    void createNotice(NoticeCreateDTO createDTO);

    void updateNotice(NoticeUpdateDTO updateDTO);

    void publishNotice(Long id);

    void archiveNotice(Long id);

    void deleteNotice(Long id);
}
package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.LogQueryDTO;
import com.suse.campus_rent.vo.admin.SystemLogVO;

import java.util.Map;

/**
 * SystemLogService
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/2 10:59
 */
public interface SystemLogService {

    Page<SystemLogVO> getSystemLog(LogQueryDTO queryDTO);

    Map<String, Object> getStatistics();
}

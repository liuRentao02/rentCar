package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.LogQueryDTO;
import com.suse.campus_rent.service.admin.service.SystemLogService;
import com.suse.campus_rent.vo.admin.SystemLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SystemLogController
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/2 11:11
 */
@RestController
@RequestMapping("/admin/systemLog")
@RequiredArgsConstructor
public class SystemLogController {
    private final SystemLogService systemLogService;

    @PostMapping("/list")
    public Result<Page<SystemLogVO>> getSystemLog(@RequestBody LogQueryDTO queryDTO) {
        Page<SystemLogVO> systemLog = systemLogService.getSystemLog(queryDTO);
        return Result.success(systemLog);
    }

    @GetMapping("/statistics")
    public Result<?> getStatistics() {
        Map<String, Object> res = systemLogService.getStatistics();
        return Result.success(res);
    }
}

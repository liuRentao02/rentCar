package com.suse.campus_rent.controller.admin;

import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.service.admin.service.DashboardService;
import com.suse.campus_rent.vo.admin.DashboardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminDashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取首页仪表盘统计数据
     */
    @GetMapping("/stats")
    public Result<DashboardVO> getDashboardStats() {
        DashboardVO vo = dashboardService.getDashboardData();
        return Result.success(vo);
    }
}
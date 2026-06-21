package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.UserCreateDTO;
import com.suse.campus_rent.dto.admin.UserQueryDTO;
import com.suse.campus_rent.dto.admin.UserUpdateDTO;
import com.suse.campus_rent.service.admin.service.UserService;
import com.suse.campus_rent.vo.admin.StatisticsVO;
import com.suse.campus_rent.vo.admin.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminUserController {

    private final UserService userService;

    /**
     * 获取用户统计数据
     */
    @GetMapping("/statistics")
    public Result<StatisticsVO> getStatistics() {
        return Result.success(userService.getStatistics());
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping
    public Result<IPage<UserVO>> listUsers(UserQueryDTO queryDTO) {
        IPage<UserVO> page = userService.listUsers(queryDTO);
        return Result.success(page);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserDetail(@PathVariable Long id) {
        return Result.success(userService.getUserDetail(id));
    }

    /**
     * 新增用户
     */
    @OperLog(title = "新增用户", category = "USER", level = "INFO")
    @PostMapping
    public Result<?> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        userService.createUser(createDTO);
        return Result.success("用户添加成功");
    }

    /**
     * 更新用户
     */
    @OperLog(title = "修改用户", category = "USER", level = "INFO")
    @PutMapping("/{id}")
    public Result<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO updateDTO) {
        // 确保路径id与DTO中id一致
        updateDTO.setId(id);
        userService.updateUser(updateDTO);
        return Result.success("用户更新成功");
    }

    /**
     * 删除用户（删除）
     */
    @OperLog(title = "删除用户", category = "USER", level = "WARN")
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("用户删除成功");
    }
}

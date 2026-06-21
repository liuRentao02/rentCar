package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.service.admin.service.ContactMessageService;
import com.suse.campus_rent.vo.admin.ContactMessageVO;
import com.suse.campus_rent.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/contact")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactMessageService contactMessageService;

    @GetMapping("/messages")
    public Result<IPage<ContactMessageVO>> listMessages(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        IPage<ContactMessageVO> pageResult = contactMessageService.listMessages(page, size, keyword);
        return Result.success(pageResult);
    }

    @DeleteMapping("/messages/{id}")
    public Result<?> deleteMessage(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return Result.success("删除成功");
    }
}
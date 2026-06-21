package com.suse.campus_rent.controller.app;

import com.suse.campus_rent.dto.app.ContactSubmitDTO;
import com.suse.campus_rent.service.app.service.ContactService;
import com.suse.campus_rent.common.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody @Valid ContactSubmitDTO dto) {
        contactService.submitMessage(dto);
        return Result.success("留言提交成功");
    }
}
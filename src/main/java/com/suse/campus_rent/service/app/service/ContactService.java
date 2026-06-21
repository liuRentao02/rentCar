package com.suse.campus_rent.service.app.service;

import com.suse.campus_rent.dto.app.ContactSubmitDTO;

public interface ContactService {
    void submitMessage(ContactSubmitDTO dto);
}
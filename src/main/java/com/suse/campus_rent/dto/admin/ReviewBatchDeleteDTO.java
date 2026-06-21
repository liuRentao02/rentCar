package com.suse.campus_rent.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class ReviewBatchDeleteDTO {
    private List<Long> ids;
}
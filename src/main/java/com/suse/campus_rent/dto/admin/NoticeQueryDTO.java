package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeQueryDTO extends QueryDTO {
    private String keyword;
    private String type;
    private String status;
    private String publishDate;  // YYYY-MM-DD
}
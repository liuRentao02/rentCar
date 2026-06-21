package com.suse.campus_rent.dto.app;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class ReviewSubmitDTO {
    private Long orderId;

    private Long carId;

    @NotNull(message = "评分不能为空")
    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 500, message = "评价内容最多500字")
    private String content;

    private String images;
}
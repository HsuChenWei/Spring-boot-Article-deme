package com.test123.demo.model.PostsReports;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;


@Data
public class PostsReportsDto {

    @Schema(description = "檢舉理由")
    private String reason;

}

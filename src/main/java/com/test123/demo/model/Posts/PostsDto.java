package com.test123.demo.model.Posts;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostsDto {


    @Schema(description = "文章編號")
    private String postId;

    @Schema(description = "會員編號")
    private String userId;

    @Schema(description = "文章名稱")
    private String title;

    @Schema(description = "文章內文")
    private String content;

    @Schema(description = "創建時間")
    private LocalDateTime creatAt;

    @Schema(description = "更新時間")
    private LocalDateTime updateAt;

}

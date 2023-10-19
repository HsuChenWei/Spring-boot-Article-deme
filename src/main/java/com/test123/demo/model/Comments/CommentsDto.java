package com.test123.demo.model.Comments;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsDto {

    private String commentId;

    private String userId;

    private String content;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private String status;


}

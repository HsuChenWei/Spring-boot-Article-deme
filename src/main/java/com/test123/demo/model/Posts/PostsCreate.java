package com.test123.demo.model.Posts;


import lombok.Data;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Data
public class PostsCreate {

    private String title;

    private String content;

}

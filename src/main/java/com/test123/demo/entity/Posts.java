package com.test123.demo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.test123.demo.hibernate.SnowflakeIDGenerator")
    @Column(name = "post_id")
    private String id;

//    @Column(name = "user_id")
//    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable=true, updatable=true)
    private User user;
}

package com.test123.demo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments_reports")
public class CommentsReports {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.test123.demo.hibernate.SnowflakeIDGenerator")
    @Column(name = "report_id")
    private String id;

    @Column(name = "comment_id")
    private String commentId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false)
    private User user;

    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id",insertable=false, updatable=false)
    private Comments comments;
}

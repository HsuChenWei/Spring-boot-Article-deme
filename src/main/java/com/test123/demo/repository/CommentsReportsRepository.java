package com.test123.demo.repository;

import com.test123.demo.entity.CommentsReports;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.beans.JavaBean;

@Repository
public interface CommentsReportsRepository extends JpaRepository<CommentsReports, String> {
    Option<CommentsReports> findByUserIdAndCommentId(String currentUserId, String commentId);
}

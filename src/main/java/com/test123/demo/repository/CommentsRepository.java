package com.test123.demo.repository;

import com.test123.demo.entity.Comments;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, String> {
    Option<Comments> findByIdAndUserId(String postId, String currentUserId);
}

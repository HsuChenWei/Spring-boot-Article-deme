package com.test123.demo.repository;

import com.test123.demo.entity.PostsReports;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsReportsRepository extends JpaRepository<PostsReports, String> {

    Option<PostsReports> findByUserIdAndPostId(String userId, String postId);

    List<PostsReports> findAllByPostId(String id);
}

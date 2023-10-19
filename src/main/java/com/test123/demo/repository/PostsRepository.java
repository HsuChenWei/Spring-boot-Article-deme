package com.test123.demo.repository;

import com.test123.demo.entity.Posts;
import io.vavr.control.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, String> {
    Page<Posts> findAll(Specification<Posts> spec, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.id = :postId AND p.userId = :userId")
    Option<Posts> findByIdAndUserId(@Param("postId") String id,@Param("userId") String userId);
}

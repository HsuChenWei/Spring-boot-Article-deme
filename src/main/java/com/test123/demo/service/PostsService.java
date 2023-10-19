package com.test123.demo.service;

import com.test123.demo.entity.Posts;
import com.test123.demo.model.Posts.PostsCreate;
import com.test123.demo.model.Posts.PostsUpdate;
import io.vavr.control.Option;

import java.time.LocalDateTime;
import java.util.List;

public interface PostsService {

    Option<Posts> getPostById(String id);

    List<Posts> getAllPosts();

    List<Posts> getFilterPosts(int page, int size, String postId, String userId, String title, String content);

    Option<Posts> createPost(PostsCreate creation);

    void deletePost(String id);

    Option<Posts> updatePost(String id, PostsUpdate update);


}

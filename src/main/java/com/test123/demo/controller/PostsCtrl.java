package com.test123.demo.controller;


import com.test123.demo.model.Posts.PostsCreate;
import com.test123.demo.model.Posts.PostsDto;
import com.test123.demo.model.Posts.PostsUpdate;
import com.test123.demo.model.wrapper.RespWrapper;
import com.test123.demo.service.PostsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@Tag(name = "Posts" ,description = "文章功能")
public class PostsCtrl {

    @Autowired
    private PostsService postsService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "單一文章查詢")
    @GetMapping("/{id}")
    public RespWrapper<PostsDto> getPost(@PathVariable String id){
        return postsService.getPostById(id)
                .map(posts -> modelMapper.map(posts, PostsDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new RuntimeException("Post not found."));
    }

    @Operation(summary = "條件查詢")
    @GetMapping("/filter")
    public RespWrapper<List<PostsDto>> getFilterPosts(
            @RequestParam(defaultValue = "0") @Parameter(description = "分頁索引 (0-based)", required = true) int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "分頁大小", required = true) int size,
            @RequestParam(required = false) @Parameter(description = "文章編號") String postId,
            @RequestParam(required = false) @Parameter(description = "使用者ID") String userId,
            @RequestParam(required = false) @Parameter(description = "文章名稱") String title,
            @RequestParam(required = false) @Parameter(description = "文章內容") String content
    ){
        return RespWrapper.success(postsService.getFilterPosts(page, size, postId, userId,title,content)
                .stream()
                .map(posts -> modelMapper.map(posts, PostsDto.class))
                .collect(Collectors.toList()));
    }

    @Operation(summary = "新增文章")
    @PostMapping("/creation")
    public RespWrapper<PostsDto> createPost(@RequestBody PostsCreate create){
        return postsService.createPost(create)
                .flatMap(posts -> postsService.getPostById(posts.getId()))
                .map(posts -> modelMapper.map(posts, PostsDto.class))
                .map(RespWrapper::success)
                .get();
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "更新文章")
    public RespWrapper<PostsDto> updatePost(@PathVariable String id,@RequestBody PostsUpdate body){
        return postsService.updatePost(id, body)
                .map(posts -> modelMapper.map(posts, PostsDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new RuntimeException("Post not found."));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "刪除文章")
    public RespWrapper<Void> deletePost(@PathVariable String id){
        postsService.deletePost(id);
        return RespWrapper.success(null);
    }
}

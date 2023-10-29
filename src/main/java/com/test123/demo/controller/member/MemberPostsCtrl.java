package com.test123.demo.controller.member;


import com.test123.demo.entity.Posts;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/member/post")
@Tag(name = "Posts" ,description = "文章功能")
public class MemberPostsCtrl {

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

    @Operation(summary = "所有文章")
    @GetMapping("/allPosts")
    public RespWrapper<List<PostsDto>> getOnShelfPosts() {
        return RespWrapper.success(postsService.getAllOnShelfPosts()
                .stream()
                .map(p -> modelMapper.map(p, PostsDto.class))
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

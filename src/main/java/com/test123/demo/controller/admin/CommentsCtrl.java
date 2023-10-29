package com.test123.demo.controller.admin;


import com.test123.demo.model.Comments.CommentsCreate;
import com.test123.demo.model.Comments.CommentsDto;
import com.test123.demo.model.Comments.CommentsUpdate;
import com.test123.demo.model.wrapper.RespWrapper;
import com.test123.demo.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Option;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/comments")
@Tag(name = "Comments", description = "留言系統")
public class CommentsCtrl {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private ModelMapper modelMapper;


    @Operation(summary = "新增留言")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/post/{postId}/comment")
    public RespWrapper<CommentsDto> createComment(@PathVariable String postId,@RequestBody CommentsCreate creation){
        return commentsService.createComments(postId,creation)
                .flatMap(comments -> Option.of(modelMapper.map(comments, CommentsDto.class)))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new RuntimeException("Failed to create comment"));
    }

    @Operation(summary = "更新留言")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{commentId}/updateComment")
    public RespWrapper<CommentsDto> updateComment(@PathVariable String commentId, @RequestBody CommentsUpdate update){
        return commentsService.updateComment(commentId, update)
                .map(u -> RespWrapper.success(modelMapper.map(u, CommentsDto.class)))
                .getOrElseThrow(() -> new RuntimeException("Failed to update comment."));
    }

    //少一個更改留言狀態(下架留言)
}

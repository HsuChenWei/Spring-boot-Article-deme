package com.test123.demo.controller.member;

import com.test123.demo.model.PostsReports.PostReportUpdateStatus;
import com.test123.demo.model.PostsReports.PostsReportsDto;
import com.test123.demo.model.wrapper.RespWrapper;
import com.test123.demo.service.PostsReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jdo.annotations.Transactional;

@RestController
@RequestMapping("/api/member/postsReports")
@Tag(name = "Posts - Report", description = "文章檢舉系統")
public class MemberPostsReportsCtrl {

    @Autowired
    private PostsReportsService postsReportsService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "檢舉文章")
    @PostMapping("/{postId}/report")
    public RespWrapper<PostsReportsDto> reportPost(@PathVariable String postId, @RequestBody PostsReportsDto body) {
        return postsReportsService.reportPost(postId, body)
                .map(report -> modelMapper.map(report, PostsReportsDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new RuntimeException("Report failed."));
    }

}

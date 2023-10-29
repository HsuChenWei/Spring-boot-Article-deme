package com.test123.demo.controller.admin;

import com.test123.demo.model.PostsReports.PostReportUpdateStatus;
import com.test123.demo.model.PostsReports.PostsReportsDto;
import com.test123.demo.model.wrapper.RespWrapper;
import com.test123.demo.service.PostsReportsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jdo.annotations.Transactional;

@RestController
@RequestMapping("/api/postsReports")
@Tag(name = "Posts - Report", description = "文章檢舉系統")
public class PostsReportsCtrl {

    @Autowired
    private PostsReportsService postsReportsService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/{postId}/report")
    @Transactional
    public RespWrapper<PostsReportsDto> reportPost(@PathVariable String postId, @RequestBody PostsReportsDto body) {
        return postsReportsService.reportPost(postId, body)
                .map(report -> modelMapper.map(report, PostsReportsDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new RuntimeException("Report failed."));
    }

    @PutMapping("/updateStatus/{id}")
    @Transactional
    public RespWrapper<PostsReportsDto> updateStatus(@PathVariable String id, @RequestBody PostReportUpdateStatus body){
        return postsReportsService
                .updatePostReportStatus(id, body)
                .map(update ->RespWrapper.success(modelMapper.map(update, PostsReportsDto.class)))
                .getOrElseThrow(() -> new RuntimeException("error"));
    }
}

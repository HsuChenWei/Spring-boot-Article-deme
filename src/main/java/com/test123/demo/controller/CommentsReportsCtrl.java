package com.test123.demo.controller;


import com.test123.demo.model.CommentsReports.CommentsReportUpdate;
import com.test123.demo.model.CommentsReports.CommentsReportsDto;
import com.test123.demo.model.wrapper.RespWrapper;
import com.test123.demo.service.CommentsReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commentsReports")
@Tag(name = "Comments - Report", description = "留言檢舉系統")
public class CommentsReportsCtrl {

    @Autowired
    private CommentsReportsService commentsReportsService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "狀態更改")
    @PutMapping("/update/{commentReportId}")
    public RespWrapper<CommentsReportsDto> updateStatus(@PathVariable String commentReportId, CommentsReportUpdate update){
        return commentsReportsService.updateStatus(commentReportId, update)
                .map(u -> RespWrapper.success(modelMapper.map(u , CommentsReportsDto.class)))
                .getOrElseThrow(() -> new RuntimeException("Failed to update status"));
    }

    @Operation(summary = "留言檢舉")
    @PostMapping("/report/{commentId}")
    public RespWrapper<CommentsReportsDto> reportComments(String commentId, CommentsReportsDto body){
        return commentsReportsService.reportComment(commentId, body)
                .map(r -> RespWrapper.success(modelMapper.map(r, CommentsReportsDto.class)))
                .getOrElseThrow(() -> new RuntimeException("Failed to report comment"));
    }
}

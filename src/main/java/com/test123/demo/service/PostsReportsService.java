package com.test123.demo.service;

import com.test123.demo.entity.PostsReports;
import com.test123.demo.model.PostsReports.PostReportUpdateStatus;
import com.test123.demo.model.PostsReports.PostsReportsDto;
import io.vavr.control.Option;

public interface PostsReportsService {

    Option<PostsReports> reportPost(String postId, PostsReportsDto body);

    Option<PostsReports> updatePostReportStatus(String postReportId, PostReportUpdateStatus status);

}

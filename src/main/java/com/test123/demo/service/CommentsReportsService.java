package com.test123.demo.service;

import com.test123.demo.entity.CommentsReports;
import com.test123.demo.model.CommentsReports.CommentsReportUpdate;
import com.test123.demo.model.CommentsReports.CommentsReportsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public interface CommentsReportsService {

    Option<CommentsReports> reportComment(String commentId, CommentsReportsDto reason);

    Option<CommentsReports> updateStatus(String commentReportId, CommentsReportUpdate update);
}

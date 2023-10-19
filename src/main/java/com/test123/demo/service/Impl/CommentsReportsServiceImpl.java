package com.test123.demo.service.Impl;

import com.test123.demo.entity.Comments;
import com.test123.demo.entity.CommentsReports;
import com.test123.demo.model.CommentsReports.CommentsReportUpdate;
import com.test123.demo.model.CommentsReports.CommentsReportsDto;
import com.test123.demo.repository.CommentsReportsRepository;
import com.test123.demo.repository.CommentsRepository;
import com.test123.demo.service.CommentsReportsService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentsReportsServiceImpl implements CommentsReportsService {

    @Autowired
    private CommentsReportsRepository commentsReportsRepository;

    @Autowired
    private CommentsRepository commentsRepository;


    @Override
    public Option<CommentsReports> reportComment(String commentId, CommentsReportsDto reason) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String currentUserId = auth.getName();

            Option<CommentsReports> existingReport = commentsReportsRepository.findByUserIdAndCommentId(currentUserId, commentId);
            if (!existingReport.isEmpty()) {
                throw new RuntimeException("CommentId isn't exist.");
            }
            Optional<Comments> commentsOptional = commentsRepository.findById(commentId);
            if (!commentsOptional.isPresent()) {
                throw new RuntimeException("Comment isn't exist");
            }

            if (commentsOptional.get().getUserId().equals(currentUserId)) {
                throw new RuntimeException("Users cannot report their own posts");
            }

            CommentsReports commentReport = new CommentsReports();
            commentReport.setCommentId(commentId);
            commentReport.setUserId(auth.getName());
            commentReport.setReason(reason.getReason());
            commentReport.setReportDate(LocalDateTime.now());
            commentReport.setStatus("0");

            commentsReportsRepository.save(commentReport);
            return Option.of(commentReport);
        }
        return Option.none();
    }

    @Override
    public Option<CommentsReports> updateStatus(String commentReportId, CommentsReportUpdate update) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Optional<CommentsReports> optionalCommentsReports = commentsReportsRepository.findById(commentReportId);
            if (!optionalCommentsReports.isPresent()) {
                throw new RuntimeException("CommentsReportsId isn't exist.");
            }
            CommentsReports updateStatus = optionalCommentsReports.get();

            updateStatus.setStatus(update.getStatus());

            commentsReportsRepository.save(updateStatus);
            return Option.of(updateStatus);
        }
        return Option.none();
    }
}

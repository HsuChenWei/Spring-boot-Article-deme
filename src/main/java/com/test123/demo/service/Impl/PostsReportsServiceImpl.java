package com.test123.demo.service.Impl;

import com.test123.demo.entity.Posts;
import com.test123.demo.entity.PostsReports;
import com.test123.demo.model.PostsReports.PostReportUpdateStatus;
import com.test123.demo.model.PostsReports.PostsReportsDto;
import com.test123.demo.repository.PostsReportsRepository;
import com.test123.demo.repository.PostsRepository;
import com.test123.demo.service.Impl.querydsl.QuerydslRepository;
import com.test123.demo.service.PostsReportsService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostsReportsServiceImpl implements PostsReportsService {

    @Autowired
    private PostsReportsRepository postsReportsRepository;

    @Autowired
    private QuerydslRepository querydsl;

    @Autowired
    private PostsRepository postsRepository;


    @Override
    public Option<PostsReports> reportPost(String postId,PostsReportsDto body) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            String currentUserId = authentication.getName();
            Option<PostsReports> existingReport = postsReportsRepository.findByUserIdAndPostId(currentUserId, postId);
            if (!existingReport.isEmpty()) {
                throw new RuntimeException("You have already reported this post.");
            }
            Optional<Posts> postOptional = postsRepository.findById(postId);
            if (!postOptional.isPresent()) {
                throw new RuntimeException("Post not found");
            }

            if (postOptional.get().getUser().equals(currentUserId)) {
                throw new RuntimeException("Users cannot report their own posts");
            }

            PostsReports newReport = new PostsReports();
            newReport.setPostId(postId);
            newReport.setUserId(currentUserId);
            newReport.setReason(body.getReason());
            newReport.setReportDate(LocalDateTime.now());
            newReport.setStatus("0");

            PostsReports savedReport = postsReportsRepository.save(newReport);
            return Option.of(savedReport);
        }else {
            throw new RuntimeException("Please login first.");
        }
    }

    @Override
    @Transactional
    public Option<PostsReports> updatePostReportStatus(String postReportId, PostReportUpdateStatus status) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){

            Optional<PostsReports> postsReportsOptional = postsReportsRepository.findById(postReportId);

            if(!postsReportsOptional.isPresent()){
                throw new RuntimeException("PostReport not exist.");
            }
            PostsReports postsReports =  postsReportsOptional.get();
            String postId = postsReports.getPostId();
            Optional<Posts> postOptional = postsRepository.findById(postId);
            if (!postOptional.isPresent()) {
                throw new RuntimeException("Post not found");
            }
            Posts post = postOptional.get();
            post.setStatus("0");
            postsRepository.save(post);

            postsReports.setStatus(status.getStatus());
            PostsReports updatedPostReport = postsReportsRepository.save(postsReports);

            return Option.of(updatedPostReport);
        }
        return Option.none();
    }

}

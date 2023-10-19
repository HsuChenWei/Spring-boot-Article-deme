package com.test123.demo.service.Impl;

import com.test123.demo.entity.Comments;
import com.test123.demo.entity.Posts;
import com.test123.demo.model.Comments.CommentsCreate;
import com.test123.demo.model.Comments.CommentsUpdate;
import com.test123.demo.repository.CommentsRepository;
import com.test123.demo.repository.PostsRepository;
import com.test123.demo.service.CommentsService;
import com.test123.demo.service.Impl.querydsl.QuerydslRepository;
import com.test123.demo.service.PostsService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private QuerydslRepository querydsl;

    @Autowired
    private PostsRepository postsRepository;

    @Override
    @Transactional
    public Option<Comments> createComments(String postId, CommentsCreate creation) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            Optional<Posts> postsOptional = postsRepository.findById(postId);
            if (!postsOptional.isPresent()) {
                throw new RuntimeException("Post isn't exist.");
            }

            String currentUserId = auth.getName();


            Comments newComment = new Comments();
            newComment.setPostId(postId);
            newComment.setUserId(currentUserId);
            newComment.setContent(creation.getContent());
            newComment.setCreateAt(LocalDateTime.now());
            newComment.setStatus("1");
            Comments savedComment = commentsRepository.save(newComment);
            return Option.of(savedComment);

        }
        return Option.none();
    }

    @Override
    public Option<Comments> updateComment(String commentId, CommentsUpdate update) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated()){
            String currentUserId = auth.getName();;
            Option<Comments> comments = commentsRepository.findByIdAndUserId(commentId, currentUserId);
            if (!comments.isEmpty()){
                Comments updateComment = comments.get();

                updateComment.setContent(update.getContent());
                updateComment.setUpdateAt(LocalDateTime.now());

                commentsRepository.save(updateComment);
                return Option.of(updateComment);
            }
        }else {
            throw new RuntimeException("Please login first.");
        }
        return Option.none();
    }
}

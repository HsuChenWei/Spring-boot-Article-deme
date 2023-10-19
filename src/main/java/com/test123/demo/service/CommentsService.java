package com.test123.demo.service;

import com.test123.demo.entity.Comments;
import com.test123.demo.model.Comments.CommentsCreate;
import com.test123.demo.model.Comments.CommentsUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.vavr.control.Option;

public interface CommentsService {

    Option<Comments> createComments(String postId, CommentsCreate creation);

    Option<Comments> updateComment(String commentId, CommentsUpdate update);
}

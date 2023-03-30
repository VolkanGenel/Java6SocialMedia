package com.volkan.service;

import com.volkan.repository.ICommentRepository;
import com.volkan.repository.IPostRepository;
import com.volkan.repository.entity.Comment;
import com.volkan.repository.entity.Post;
import com.volkan.utility.ServiceManager;
import org.springframework.data.mongodb.repository.MongoRepository;

public class CommentService extends ServiceManager<Comment,String> {
    private final ICommentRepository commentRepository;

    public CommentService(ICommentRepository commentRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
    }
}

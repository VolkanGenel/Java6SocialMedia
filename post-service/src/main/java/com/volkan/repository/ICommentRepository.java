package com.volkan.repository;

import com.volkan.repository.entity.Comment;
import com.volkan.repository.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends MongoRepository<Comment,String> {
}

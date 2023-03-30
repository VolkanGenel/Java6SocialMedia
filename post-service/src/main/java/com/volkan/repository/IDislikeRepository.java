package com.volkan.repository;

import com.volkan.repository.entity.Dislike;
import com.volkan.repository.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDislikeRepository extends MongoRepository<Dislike,String> {
}

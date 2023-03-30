package com.volkan.service;

import com.volkan.dto.request.CreateNewPostRequestDto;
import com.volkan.mapper.IPostMapper;
import com.volkan.repository.IPostRepository;
import com.volkan.repository.entity.Post;
import com.volkan.utility.ServiceManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService extends ServiceManager<Post,String> {

    private final IPostRepository postRepository;

    public PostService(IPostRepository postRepository) {
        super(postRepository);
        this.postRepository = postRepository;
    }

    public Post createPost(CreateNewPostRequestDto dto) {
        return save(IPostMapper.INSTANCE.toPost(dto));
    }
}

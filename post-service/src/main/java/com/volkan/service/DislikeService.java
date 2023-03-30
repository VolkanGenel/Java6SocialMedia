package com.volkan.service;

import com.volkan.repository.IDislikeRepository;
import com.volkan.repository.entity.Dislike;
import com.volkan.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class DislikeService extends ServiceManager<Dislike,String> {
    private final IDislikeRepository dislikeRepository;


    public DislikeService(IDislikeRepository dislikeRepository) {
        super(dislikeRepository);
        this.dislikeRepository = dislikeRepository;
    }
}

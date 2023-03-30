package com.volkan.service;

import com.volkan.repository.ILikeRepository;
import com.volkan.repository.entity.Like;
import com.volkan.utility.ServiceManager;

public class LikeService extends ServiceManager<Like,String> {
    private final ILikeRepository likeRepository;

    public LikeService(ILikeRepository likeRepository) {
        super(likeRepository);
        this.likeRepository = likeRepository;
    }
}

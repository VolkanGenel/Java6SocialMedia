package com.volkan.service;

import com.volkan.mapper.IElasticMapper;
import com.volkan.rabbitmq.model.RegisterElasticModel;
import com.volkan.repository.IUserProfileRepository;
import com.volkan.repository.entity.UserProfile;
import com.volkan.utility.ServiceManager;
import org.springframework.stereotype.Service;

/**
 * user microservisinde findbyrole diye bir not yazalım bu method girdiğimiz role gore
 * bize databasedeki userprofile ları dönsün ayrıca bu methodu cachleyelim.
 * Bir de bu cash ne zaman silinir bununla ilgili kod eklemelerini de yapalım.
 */
@Service
public class UserProfileService extends ServiceManager<UserProfile,String> {
    private final IUserProfileRepository userProfileRepository;


    public UserProfileService(IUserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile createUserWithRabbitMq(RegisterElasticModel model) {
        return save(IElasticMapper.INSTANCE.toUserProfile(model));
    }
}

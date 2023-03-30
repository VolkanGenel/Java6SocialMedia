package com.volkan.service;

import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.request.UpdateEmailOrUsernameRequestDto;
import com.volkan.dto.request.UpdateUserRequestDto;
import com.volkan.dto.response.ActivateStatusDto;
import com.volkan.exception.EErrorType;
import com.volkan.exception.UserManagerException;
import com.volkan.manager.IAuthManager;
import com.volkan.mapper.IUserMapper;
import com.volkan.rabbitmq.model.RegisterModel;
import com.volkan.rabbitmq.producer.RegisterProducer;
import com.volkan.repository.IUserProfileRepository;
import com.volkan.repository.entity.UserProfile;
import com.volkan.repository.enums.EStatus;
import com.volkan.utility.JwtTokenManager;
import com.volkan.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * user microservisinde findbyrole diye bir not yazalım bu method girdiğimiz role gore
 * bize databasedeki userprofile ları dönsün ayrıca bu methodu cachleyelim.
 * Bir de bu cash ne zaman silinir bununla ilgili kod eklemelerini de yapalım.
 */
@Service
public class UserProfileService extends ServiceManager<UserProfile,String> {
    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenManager tokenManager;
    private final IAuthManager authManager;
    private final CacheManager cacheManager;
    private final RegisterProducer registerProducer;

    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenManager tokenManager, IAuthManager authManager, CacheManager cacheManager, RegisterProducer registerProducer) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.tokenManager = tokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
    }

    public Boolean createUser(NewCreateUserRequestDto dto) {
        try {
            save(IUserMapper.INSTANCE.toUserProfile(dto));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(EErrorType.USER_NOT_CREATED);
        }

    }

    public Boolean createUserWithRabbitMq(RegisterModel model) {
        try {
            UserProfile userProfile = save(IUserMapper.INSTANCE.toUserProfile(model));
            //burada rabbitmq  ile elastic-service veri gönderelecek.
            registerProducer.sendNewUser(IUserMapper.INSTANCE.toRegisterElasticModel(userProfile));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(EErrorType.USER_NOT_CREATED);
        }

    }

    public Boolean activateStatus(String token) {
        Optional<Long> authId = tokenManager.getIdFromToken(token.substring(7));
        if (authId.isEmpty()) {
            throw new UserManagerException(EErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile =userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty())
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    public Boolean updateUser(UpdateUserRequestDto dto) {
        Optional<Long> authId = tokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserManagerException(EErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()) {
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        }
        cacheManager.getCache("findbyusername").evict(userProfile.get().getUsername().toLowerCase());
        if (!dto.getUsername().equals(userProfile.get().getUsername())||!dto.getEmail().equals(userProfile.get().getEmail())) {
            userProfile.get().setUsername(dto.getUsername());
            UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = IUserMapper.INSTANCE.toUpdateEmailOrUsernameRequestDto(dto);
            updateEmailOrUsernameRequestDto.setAuthId(authId.get());
            authManager.updateEmailOrUsername("Bearer "+dto.getToken(), updateEmailOrUsernameRequestDto);
        }
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAvatar(dto.getAvatar());
        userProfile.get().setAdres(dto.getAdres());
        userProfile.get().setEmail(dto.getEmail());
        userProfile.get().setAbout(dto.getAbout());
        update(userProfile.get());
        return true;
    }

    public Boolean deactivateStatus(Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty())
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }
    public Boolean delete (Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty())
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }

    @Cacheable(value = "findbyusername", key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username) {
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByUsernameIgnoreCase(username);
        if (userProfile.isEmpty()) {
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    @Cacheable(value = "findbyrole", key = "#role.toUpperCase()")
    public List<UserProfile> findByRole(String role,String token) {
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Long> authIds = authManager.findByRole(token,role).getBody();
        //ResponseEntity<List<Long>> authIds2 = authManager.findByRole(role);

        return authIds.stream().map(x-> userProfileRepository.findOptionalByAuthId(x)
                .orElseThrow(()->{throw new UserManagerException(EErrorType.USER_NOT_FOUND);}))
                .collect(Collectors.toList());
    }

    public Optional<UserProfile> findByAuthId(Long authId) {
        return userProfileRepository.findOptionalByAuthId(authId);
    }
}

package com.volkan.service;

import com.volkan.dto.request.CreateFollowRequestDto;
import com.volkan.exception.EErrorType;
import com.volkan.exception.UserManagerException;
import com.volkan.mapper.IFollowMapper;
import com.volkan.repository.IFollowRepository;
import com.volkan.repository.entity.Follow;
import com.volkan.repository.entity.UserProfile;
import com.volkan.utility.JwtTokenManager;
import com.volkan.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FollowService extends ServiceManager {
    private final IFollowRepository followRepository;
    private final JwtTokenManager jwtTokenManager;
    private final UserProfileService userProfileService;

    public FollowService(IFollowRepository followRepository, JwtTokenManager jwtTokenManager, UserProfileService userProfileService) {
        super(followRepository);
        this.followRepository = followRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userProfileService = userProfileService;
    }
    @Transactional
    public Boolean createFollow(CreateFollowRequestDto dto) {
        Follow follow;
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserManagerException(EErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileService.findByAuthId(authId.get());
        Optional<UserProfile> followUser = userProfileService.findById(dto.getFollowId());
        Optional<Follow> followDB =
                followRepository.findByUserIdAndFollowId(userProfile.get().getId(),followUser.get().getId());
        if (followDB.isPresent()) {
            throw new UserManagerException(EErrorType.FOLLOW_REQUEST_ALREADY_EXISTS);
        }
        if(userProfile.isPresent()&&followUser.isPresent()) {
            follow = IFollowMapper.INSTANCE.toFollow(dto,userProfile.get().getId());
            follow = (Follow) save(follow);
            userProfile.get().getFollows().add(followUser.get().getId());
            followUser.get().getFollower().add(userProfile.get().getId());
            userProfileService.update(userProfile.get());
            userProfileService.update(followUser.get());
        } else {
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        }
        return true;
    }
}

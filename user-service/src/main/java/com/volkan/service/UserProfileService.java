package com.volkan.service;

import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.request.UpdateEmailOrUsernameRequestDto;
import com.volkan.dto.request.UpdateUserRequestDto;
import com.volkan.exception.EErrorType;
import com.volkan.exception.UserManagerException;
import com.volkan.manager.IAuthManager;
import com.volkan.mapper.IUserMapper;
import com.volkan.repository.IUserProfileRepository;
import com.volkan.repository.entity.UserProfile;
import com.volkan.repository.enums.EStatus;
import com.volkan.utility.JwtTokenManager;
import com.volkan.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile,Long> {
    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenManager tokenManager;
    private final IAuthManager authManager;

    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenManager tokenManager, IAuthManager authManager) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.tokenManager = tokenManager;
        this.authManager = authManager;
    }

    public Boolean createUser(NewCreateUserRequestDto dto) {
        try {
            save(IUserMapper.INSTANCE.toUserProfile(dto));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(EErrorType.USER_NOT_CREATED);
        }

    }

    public Boolean activateStatus(Long authId) {
        Optional<UserProfile> userProfile = findById(authId);
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
        Optional<UserProfile> userProfile = findById(authId.get());
        if (userProfile.isEmpty()) {
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        }
        if (dto.getUsername().equals(userProfile.get().getUsername())||!dto.getEmail().equals(userProfile.get().getEmail())) {
            userProfile.get().setUsername(dto.getUsername());
            UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = IUserMapper.INSTANCE.toUpdateEmailOrUsernameRequestDto(dto);
            updateEmailOrUsernameRequestDto.setAuthId(authId.get());
            authManager.updateEmailOrUsername(updateEmailOrUsernameRequestDto);
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
        Optional<UserProfile> userProfile = findById(authId);
        if (userProfile.isEmpty())
            throw new UserManagerException(EErrorType.USER_NOT_FOUND);
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }
}

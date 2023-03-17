package com.volkan.service;

import com.volkan.dto.request.*;
import com.volkan.dto.response.AuthRegisterResponseDto;
import com.volkan.exception.AuthManagerException;
import com.volkan.exception.EErrorType;
import com.volkan.manager.IUserManager;
import com.volkan.mapper.IAuthMapper;
import com.volkan.repository.IAuthRepository;
import com.volkan.repository.entity.Auth;
import com.volkan.repository.enums.EStatus;
import com.volkan.utility.CodeGenerator;
import com.volkan.utility.JwtTokenManager;
import com.volkan.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final IUserManager userManager;
    private final JwtTokenManager tokenManager;

    public AuthService(IAuthRepository authRepository, IUserManager userManager, JwtTokenManager tokenManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager= userManager;
        this.tokenManager = tokenManager;
    }
    @Transactional
    public AuthRegisterResponseDto register(AuthRegisterRequestDto dto) {
//        if (authRepository.isUsername(dto.getUsername()))
//            throw new AuthServiceException(EErrorType.REGISTER_ERROR_USERNAME);
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        /**
         * Repo -> repository.save(auth); bu bana kaydettiği entityi döner
         * Servi -> save(auth); bu da bana kaydettiği entityi döner
         * direkt -> auth, bir şekilde kayıt edilen entity nin bilgileri istenir ve bunu döner.
         */
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
        userManager.createUser(IAuthMapper.INSTANCE.toNewCreateUserRequestDto(auth));

        AuthRegisterResponseDto authRegisterResponseDto = IAuthMapper.INSTANCE.toAuthResponseDto(auth);
        return authRegisterResponseDto;
    }

    public String doLogin(DoLoginRequestDto dto) {
            Optional<Auth> auth = authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
            if (auth.isEmpty())
                throw new AuthManagerException(EErrorType.LOGIN_ERROR);
            if (!auth.get().getStatus().equals(EStatus.ACTIVE))
                throw new AuthManagerException(EErrorType.ACCOUNT_NOT_ACTIVE);
            return tokenManager.createToken(auth.get().getId()
                    ,auth.get().getRole()).orElseThrow(()-> {throw new AuthManagerException(EErrorType.TOKEN_NOT_CREATED);});
        }

    public Boolean activateStatus(ActivateRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty())
            throw new AuthManagerException(EErrorType.USER_NOT_FOUND);
        if(dto.getActivationCode().equals(auth.get().getActivationCode())) {
            auth.get().setStatus(EStatus.ACTIVE);
            update(auth.get());
            userManager.activateStatus(auth.get().getId());
            return true;
        } else {
            throw new AuthManagerException(EErrorType.ACTIVATE_CODE_ERROR);
        }
    }

    public Boolean updateEmailOrUsername(UpdateEmailOrUsernameRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(EErrorType.USER_NOT_FOUND);
        }
        auth.get().setUsername(dto.getUsername());
        auth.get().setEmail(dto.getEmail());
        update(auth.get());
        return true;
    }

    public Boolean DeactivateStatus(DeactivateRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty())
            throw new AuthManagerException(EErrorType.USER_NOT_FOUND);
        if(dto.getDeactivationCode().equals(auth.get().getActivationCode())) {
            auth.get().setStatus(EStatus.DELETED);
            update(auth.get());
            userManager.deactivateStatus(auth.get().getId());
            return true;
        } else {
            throw new AuthManagerException(EErrorType.ACTIVATE_CODE_ERROR);
        }
    }
}

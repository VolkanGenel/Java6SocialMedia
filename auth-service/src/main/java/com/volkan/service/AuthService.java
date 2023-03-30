package com.volkan.service;

import com.volkan.dto.request.*;
import com.volkan.dto.response.AuthRegisterResponseDto;
import com.volkan.exception.AuthManagerException;
import com.volkan.exception.EErrorType;
import com.volkan.manager.IUserManager;
import com.volkan.mapper.IAuthMapper;
import com.volkan.rabbitmq.producer.RegisterMailProducer;
import com.volkan.rabbitmq.producer.RegisterProducer;
import com.volkan.repository.IAuthRepository;
import com.volkan.repository.entity.Auth;
import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import com.volkan.utility.CodeGenerator;
import com.volkan.utility.JwtTokenManager;
import com.volkan.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final IUserManager userManager;
    private final JwtTokenManager tokenManager;
    private final CacheManager cacheManager;
    private final RegisterProducer registerProducer;
    private final RegisterMailProducer mailProducer;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IAuthRepository authRepository, IUserManager userManager, JwtTokenManager tokenManager, CacheManager cacheManager, RegisterProducer registerProducer, RegisterMailProducer mailProducer, PasswordEncoder passwordEncoder) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager= userManager;
        this.tokenManager = tokenManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
        this.mailProducer = mailProducer;
        this.passwordEncoder = passwordEncoder;
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

        try {
            save(auth);
            userManager.createUser(IAuthMapper.INSTANCE.toNewCreateUserRequestDto(auth));
            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        } catch (Exception e) {
            //delete(auth)
            throw new AuthManagerException(EErrorType.USER_NOT_CREATED);
        }

        AuthRegisterResponseDto authRegisterResponseDto = IAuthMapper.INSTANCE.toAuthResponseDto(auth);
        return authRegisterResponseDto;
    }
    @Transactional
    public AuthRegisterResponseDto registerWithRabbitMq(AuthRegisterRequestDto dto) {
//        if (authRepository.isUsername(dto.getUsername()))
//            throw new AuthServiceException(EErrorType.REGISTER_ERROR_USERNAME);
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        /**
         * Repo -> repository.save(auth); bu bana kaydettiği entityi döner
         * Servi -> save(auth); bu da bana kaydettiği entityi döner
         * direkt -> auth, bir şekilde kayıt edilen entity nin bilgileri istenir ve bunu döner.
         */

        auth.setActivationCode(CodeGenerator.generateCode());

        try {
            save(auth);
            // rabbitmq ile haberleşme sağlanacak
            registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));
            mailProducer.sendActivationCode(IAuthMapper.INSTANCE.toRegisterMailModel(auth));
            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        } catch (Exception e) {
            //delete(auth)
            throw new AuthManagerException(EErrorType.USER_NOT_CREATED);
        }

        AuthRegisterResponseDto authRegisterResponseDto = IAuthMapper.INSTANCE.toAuthResponseDto(auth);
        return authRegisterResponseDto;
    }

    public String doLogin(DoLoginRequestDto dto) {
            Optional<Auth> auth = authRepository.findOptionalByUsername(dto.getUsername());
            System.out.println(auth.get().getPassword());
            System.out.println(passwordEncoder.encode(dto.getPassword()));
            if (auth.isEmpty() || !passwordEncoder.matches(dto.getPassword(), auth.get().getPassword()))
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
            // user service e istek atılacak
            String token = tokenManager.createToken(auth.get().getId(),auth.get().getRole()).get();
            userManager.activateStatus("Bearer "+token);
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

    public Boolean delete(Long id) {
        Optional<Auth> auth = findById(id);
        if (auth.isEmpty())
            throw new AuthManagerException(EErrorType.USER_NOT_FOUND);
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.delete(id);
        return true;
    }

    public List<Long> findByRole(String role) {
        ERole myrole;
        try{
            myrole = ERole.valueOf(role.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            throw new AuthManagerException(EErrorType.ROLE_NOT_FOUND);
        }
        return authRepository.findAllByRole(myrole).stream().map(x->x.getId()).collect(Collectors.toList());
    }
}

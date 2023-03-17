package com.volkan.controller;

import com.volkan.dto.request.*;
import com.volkan.dto.response.AuthRegisterResponseDto;
import com.volkan.repository.entity.Auth;
import com.volkan.repository.enums.ERole;
import com.volkan.service.AuthService;
import com.volkan.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static com.volkan.constants.ApiUrls.*;

/**
 * dışarıdan login olmak için gerekli parametreleri alalım
 * eğer bilgiler doğru ise bize true yanlış ise false dönsün
 */

/**
 * status u aktif hale getirdiğim zaman user micro servisinde de statusum aktif hale gelsi istiyorum
 */

/**
 * login methodumuzu düzeltelim bize bir token üretip tokenı dönsün sadece active kullanıcılar
 * login olabilsin
 */
/**
 * delete işleminde verimizi silmiyoruz sadece statusunu değiştiriyoruz.
 */

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenManager tokenManager;
    @PostMapping(REGISTER)
    public ResponseEntity<AuthRegisterResponseDto> register(@RequestBody @Valid AuthRegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }
    @PostMapping(LOGIN)
    public ResponseEntity<String> doLogin(@RequestBody DoLoginRequestDto dto) {
        return ResponseEntity.ok(authService.doLogin(dto));
    }
    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus (@RequestBody ActivateRequestDto dto) {
        return ResponseEntity.ok(authService.activateStatus(dto));
    }
    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }
    @GetMapping("/createtoken")
    public ResponseEntity<String> createToken(Long id, ERole role) {
       return ResponseEntity.ok(tokenManager.createToken(id,role).get());
    }
    @GetMapping("/createtoken2")
    public ResponseEntity<String> createToken(Long id) {
        return ResponseEntity.ok(tokenManager.createToken(id).get());
    }
    @GetMapping("/getidfromtoken")
    public ResponseEntity<Long> getIdFromToken(String token) {
        return ResponseEntity.ok(tokenManager.getIdFromToken(token).get());
    }
    @GetMapping("/getrolefromtoken")
    public ResponseEntity<String> getRoleFromToken(String token) {
        return ResponseEntity.ok(tokenManager.getRoleFromToken(token).get());
    }
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestBody UpdateEmailOrUsernameRequestDto dto) {
        return ResponseEntity.ok(authService.updateEmailOrUsername(dto));
    }
    @DeleteMapping(DEACTIVATESTATUS)
    public ResponseEntity<Boolean> deactivateStatus (@RequestBody DeactivateRequestDto dto) {
        return ResponseEntity.ok(authService.DeactivateStatus(dto));
    }

}

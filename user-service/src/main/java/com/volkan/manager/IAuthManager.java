package com.volkan.manager;

import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.request.UpdateEmailOrUsernameRequestDto;
import com.volkan.dto.request.UpdateUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.volkan.constants.ApiUrls.CREATE;
import static com.volkan.constants.ApiUrls.UPDATE;

@FeignClient(url="http://localhost:7071/api/v1/auth",decode404 = true,name = "userprofile-auth")
public interface IAuthManager {
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestBody UpdateEmailOrUsernameRequestDto dto);
}

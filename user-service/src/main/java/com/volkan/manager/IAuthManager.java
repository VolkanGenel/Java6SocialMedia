package com.volkan.manager;

import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.request.UpdateEmailOrUsernameRequestDto;
import com.volkan.dto.request.UpdateUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.volkan.constants.ApiUrls.*;

@FeignClient(url="http://localhost:7071/api/v1/auth",decode404 = true,name = "userprofile-auth")
public interface IAuthManager {
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestHeader(value="Authorization") String token, @RequestBody UpdateEmailOrUsernameRequestDto dto);
    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<Long>> findByRole(@RequestHeader(value="Authorization") String token,
                                                 @RequestParam String role);
}

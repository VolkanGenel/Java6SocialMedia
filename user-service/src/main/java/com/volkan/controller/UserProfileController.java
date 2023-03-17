package com.volkan.controller;

import com.volkan.constants.ApiUrls;
import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.request.UpdateUserRequestDto;
import com.volkan.repository.entity.UserProfile;
import com.volkan.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.volkan.constants.ApiUrls.*;

/**
 * update methodu oluşturulacak
 */
@RestController
@RequestMapping(ApiUrls.USER)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserRequestDto dto) {
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @GetMapping(ACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId) {
    return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateUser(@RequestBody UpdateUserRequestDto dto) {
        return ResponseEntity.ok(userProfileService.updateUser(dto));
    }
    @GetMapping(DEACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> deactivateStatus(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.deactivateStatus(authId));
    }

}

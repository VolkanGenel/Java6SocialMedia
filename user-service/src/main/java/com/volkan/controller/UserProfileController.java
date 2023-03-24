package com.volkan.controller;

import com.volkan.constants.ApiUrls;
import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.request.UpdateUserRequestDto;
import com.volkan.repository.entity.UserProfile;
import com.volkan.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.volkan.constants.ApiUrls.*;

/**
 * update methodu oluşturulacak
 * findbyusername methodu yazalım bu methodu service de cacheleyelim
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
    @PutMapping(DEACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> deactivateStatus(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.deactivateStatus(authId));
    }
    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete (@RequestParam Long id) {
        return ResponseEntity.ok(userProfileService.delete(id));
    }
    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @GetMapping(FINDBYUSERNAME+"/{username}")
    public ResponseEntity<UserProfile> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }
    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<UserProfile>> findByRole(@RequestParam String role) {
        return ResponseEntity.ok(userProfileService.findByRole(role));
    }
}

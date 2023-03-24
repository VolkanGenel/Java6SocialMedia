package com.volkan.manager;



import static com.volkan.constants.ApiUrls.*;
import com.volkan.dto.request.NewCreateUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(url="http://localhost:7072/api/v1/user",decode404 = true,name = "auth-userprofile")
public interface IUserManager {
    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserRequestDto dto);
    @GetMapping(ACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);
    @PutMapping (DEACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> deactivateStatus(@PathVariable Long authId);
    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete (@RequestParam Long id);

}


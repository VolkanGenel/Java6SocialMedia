package com.volkan.manager;


import com.volkan.repository.entity.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.volkan.constants.ApiUrls.FINDALL;


@FeignClient(url="http://localhost:7072/api/v1/user",decode404 = true,name = "elastic-userprofile")
public interface IUserManager {
    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findAll();

}


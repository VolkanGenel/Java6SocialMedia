package com.volkan.utility;

import com.volkan.manager.IUserManager;
import com.volkan.repository.entity.UserProfile;
import com.volkan.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllData {

    private final UserProfileService userProfileService;
    private final IUserManager userManager;
    //@PostConstruct (Verileri aldıktan sonra silmeliyiz, yoksa verileri yeniden yüklemeye çalışır).
    public void initData() {
        List<UserProfile> userProfileList=userManager.findAll().getBody();
        userProfileService.saveAll(userProfileList);
    }
}

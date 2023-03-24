package com.volkan.rabbitmq.consumer;

import com.volkan.rabbitmq.model.RegisterElasticModel;
import com.volkan.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // console a log info çıktısı vermek için kullanılan kütüphane
public class RegisterConsumer {
    private final UserProfileService userProfileService;
    @RabbitListener(queues = ("${rabbitmq.queueregisterelastic}"))
    public void newUserCreate(RegisterElasticModel model){
        log.info("User {}",model.toString());
        userProfileService.createUserWithRabbitMq(model);
        //userProfileService.createUser(IUserMapper.INSTANCE.toNewCreateUserRequestDto(model)); 2. tercih
    }
}

package com.example.fargate.converter;

import com.example.fargate.entity.UserEntity;
import com.example.fargate.payload.UserPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserPayloadToUserEntityConverter implements Converter<UserPayload, UserEntity> {

    @Override
    public UserEntity convert(UserPayload userPayload) {
        log.debug("convert: {}", userPayload);

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userPayload.getName());

        return userEntity;
    }
}

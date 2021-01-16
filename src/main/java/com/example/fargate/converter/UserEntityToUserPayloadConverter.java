package com.example.fargate.converter;

import com.example.fargate.entity.UserEntity;
import com.example.fargate.payload.UserPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEntityToUserPayloadConverter implements Converter<UserEntity, UserPayload> {

    @Override
    public UserPayload convert(UserEntity userEntity) {
        log.debug("convert: {}", userEntity);

        return UserPayload.builder()
                .name(userEntity.getName())
                .build();
    }
}

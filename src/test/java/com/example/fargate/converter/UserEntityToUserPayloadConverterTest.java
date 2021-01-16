package com.example.fargate.converter;

import com.example.fargate.entity.UserEntity;
import com.example.fargate.payload.UserPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserEntityToUserPayloadConverterTest {

    private UserEntityToUserPayloadConverter userEntityToUserPayloadConverter;

    @BeforeEach
    void setUp() {
       this.userEntityToUserPayloadConverter = new UserEntityToUserPayloadConverter();
    }

    @Test
    void converter_entityNameNull_payloadNameNull() {

        UserEntity userEntity = new UserEntity();

        UserPayload userPayload = this.userEntityToUserPayloadConverter.convert(userEntity);

        assertNotNull(userPayload);
        assertNull(userPayload.getName());
    }

    @Test
    void converter_entityName_payloadName() {

        String name = "name";
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);

        UserPayload userPayload = this.userEntityToUserPayloadConverter.convert(userEntity);

        assertNotNull(userPayload);
        assertEquals(name, userPayload.getName());
    }
}

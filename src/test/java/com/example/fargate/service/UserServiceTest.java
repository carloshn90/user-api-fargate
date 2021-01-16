package com.example.fargate.service;

import com.example.fargate.entity.UserEntity;
import com.example.fargate.payload.UserPayload;
import com.example.fargate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ConversionService conversionServiceMock;

    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
       this.userService = new UserServiceImp(this.userRepositoryMock, this.conversionServiceMock);
    }

    @Test
    void findAllUser_emptyListFromRepository_emptyList() {

        List<UserPayload> userPayloadList = this.userService.findAllUser();

        assertThat(userPayloadList, hasSize(0));
    }

    @Test
    void findAllUser_oneUserListFromRepository_oneUserList() {

        UserEntity user = mock(UserEntity.class);

        when(this.userRepositoryMock.findAll()).thenReturn(Collections.singletonList(user));

        List<UserPayload> userPayloadList = this.userService.findAllUser();

        assertThat(userPayloadList, hasSize(1));
        verify(this.conversionServiceMock, times(1)).convert(user, UserPayload.class);
    }

    @Test
    void addUser_saveCorrectUserPayload_optionalWithUserPayload() {
        UserPayload userPayloadMock = mock(UserPayload.class);
        UserEntity userEntityMock = mock(UserEntity.class);

        when(this.conversionServiceMock.convert(userPayloadMock, UserEntity.class)).thenReturn(userEntityMock);
        when(this.userRepositoryMock.save(userEntityMock)).thenReturn(userEntityMock);

        this.userService.addUser(userPayloadMock);

        InOrder inOrder = inOrder(this.conversionServiceMock, this.userRepositoryMock);
        inOrder.verify(this.conversionServiceMock, times(1)).convert(userPayloadMock, UserEntity.class);
        inOrder.verify(this.userRepositoryMock, times(1)).save(userEntityMock);
        inOrder.verify(this.conversionServiceMock, times(1)).convert(userEntityMock, UserPayload.class);
    }
}

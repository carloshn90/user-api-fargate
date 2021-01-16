package com.example.fargate.controller;

import com.example.fargate.payload.UserPayload;
import com.example.fargate.service.UserService;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    public static final String USER_PATH = "/users";

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Mock
    private UserService userServiceMock;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        UserController userController = new UserController(this.userServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUser_emptyUserListFromService_emptyUserList() throws Exception {

        when(this.userServiceMock.findAllUser()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(
                get(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllUser_oneUserFromService_oneUserList() throws Exception {

        UserPayload userPayLoad = UserPayload.builder().name("User test").build();
        List<UserPayload> userPayloadList = Collections.singletonList(userPayLoad);

        when(this.userServiceMock.findAllUser()).thenReturn(userPayloadList);

        MvcResult mvcResult = this.mockMvc.perform(
                get(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$").isNotEmpty())
         .andReturn();

        String resultJson = mvcResult.getResponse().getContentAsString();
        CollectionType type = this.objectMapper.getTypeFactory().constructCollectionType(List.class, UserPayload.class);
        List<UserPayload> userPayloadResultList = this.objectMapper.readValue(resultJson, type);
        assertThat(userPayloadResultList, hasSize(1));
        assertEquals("User test", userPayloadResultList.get(0).getName());
    }

    @Test
    void addUser_errorSavingUser_BadRequest() throws Exception {
        UserPayload userPayload = UserPayload.builder().name("User add test").build();
        String userPayloadJson = this.objectMapper.writeValueAsString(userPayload);

        when(this.userServiceMock.addUser(any(UserPayload.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(
                post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayloadJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addUser_correctSavingUser_createdStatus() throws Exception {
        UserPayload userPayload = UserPayload.builder().name("User add test").build();
        String userPayloadJson = this.objectMapper.writeValueAsString(userPayload);

        when(this.userServiceMock.addUser(any(UserPayload.class))).thenReturn(Optional.of(userPayload));

        this.mockMvc.perform(
                post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayloadJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
         .andExpect(jsonPath("$").isNotEmpty());
    }
}

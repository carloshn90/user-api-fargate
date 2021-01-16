package com.example.fargate.controller;

import com.example.fargate.entity.UserEntity;
import com.example.fargate.payload.UserPayload;
import com.example.fargate.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "classpath:sql/controller/userControllerIT-init.sql")
@Sql(scripts = "classpath:sql/controller/userControllerIT-clean.sql", executionPhase = AFTER_TEST_METHOD)
class UserControllerIT {

    public static final String BASE_URL = "/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllUser_fromUserEntityDb_userPayloadList() throws Exception {

        MvcResult result = this.mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();

       String resultJson = result.getResponse().getContentAsString();
       CollectionType type = this.objectMapper.getTypeFactory().constructCollectionType(List.class, UserPayload.class);
       List<UserPayload> userPayloadList = this.objectMapper.readValue(resultJson, type);
       assertThat(userPayloadList, hasSize(1));
       assertEquals("user-controller-test-db", userPayloadList.get(0).getName());
    }

    @Test
    void addUser_newUserEntityDb_userCreated() throws Exception {

        String userToCreateInDb = "user to create in db";
        UserPayload userPayload = UserPayload.builder().name(userToCreateInDb).build();
        String userPayloadJson = this.objectMapper.writeValueAsString(userPayload);
        Example<UserEntity> userEntityExample = this.createUserEntityExample(userToCreateInDb);

        this.mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayloadJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$").isNotEmpty());

        assertTrue(this.userRepository.exists(userEntityExample));
    }

    private Example<UserEntity> createUserEntityExample(String name) {

        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);

        return Example.of(userEntity);
    }
}

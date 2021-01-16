package com.example.fargate.controller;

import com.example.fargate.payload.UserPayload;
import com.example.fargate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserPayload> findAllUser() {
       log.info("findAllUser: Get /users");

       return this.userService.findAllUser();
    }

    @PostMapping
    public ResponseEntity<UserPayload> addUser(@RequestBody UserPayload userPayload) {
        log.info("addUser: Post /users {}", userPayload);

        return this.userService.addUser(userPayload)
                .map(userPayloadCreated -> ResponseEntity.status(HttpStatus.CREATED).body(userPayloadCreated))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}

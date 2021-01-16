package com.example.fargate.service;

import com.example.fargate.payload.UserPayload;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserPayload> findAllUser();

    Optional<UserPayload> addUser(UserPayload userPayload);
}

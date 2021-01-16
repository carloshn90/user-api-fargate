package com.example.fargate.service;

import com.example.fargate.entity.UserEntity;
import com.example.fargate.payload.UserPayload;
import com.example.fargate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public List<UserPayload> findAllUser() {
        log.debug("findAllUser");

        return this.userRepository.findAll().stream()
                .map(this::convertEntityToPayload)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<UserPayload> addUser(UserPayload userPayload) {
        log.debug("addUser: {}", userPayload);

        return Optional.ofNullable(this.convertPayloadToEntity(userPayload))
                .map(this.userRepository::save)
                .map(this::convertEntityToPayload);
    }

    private UserEntity convertPayloadToEntity(UserPayload userPayload) {
        log.debug("convertPayloadToEntity: {}", userPayload);

        return this.conversionService.convert(userPayload, UserEntity.class);
    }

    private UserPayload convertEntityToPayload(UserEntity userEntity) {
        log.debug("convertEntityToPayload: {}", userEntity);

        return this.conversionService.convert(userEntity, UserPayload.class);
    }
}

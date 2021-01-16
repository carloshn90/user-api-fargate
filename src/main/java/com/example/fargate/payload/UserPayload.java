package com.example.fargate.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Builder
@ToString
public class UserPayload implements Serializable {

    public static final long serialVersionUID = 1049964248399720072L;

    private final String name;
}

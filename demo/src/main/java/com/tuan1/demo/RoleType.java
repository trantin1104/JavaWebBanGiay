package com.tuan1.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;
}
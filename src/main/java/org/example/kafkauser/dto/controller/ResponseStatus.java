package org.example.kafkauser.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseStatus {
    SUCCESS("200", "Request was successful"),
    FAIL("400", "Request was failed");

    private String code;
    private String description;
}

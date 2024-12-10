package org.example.kafkauser.common.exception;

import lombok.Getter;

@Getter
public class UsersException extends RuntimeException {
    private final ErrorCode errorCode;

    public UsersException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UsersException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}

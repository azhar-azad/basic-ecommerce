package com.azad.basicecommerce.common.exceptions;

import lombok.Data;
import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends RuntimeException {

    private String currentUserAccess;
    private String validAccess;

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, String currentUserAccess, String validAccess) {
        super(message);
        this.currentUserAccess = currentUserAccess;
        this.validAccess = validAccess;
    }
}

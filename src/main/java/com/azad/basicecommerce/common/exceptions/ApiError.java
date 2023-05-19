package com.azad.basicecommerce.common.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(error);
    }
}

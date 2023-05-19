package com.azad.basicecommerce.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
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

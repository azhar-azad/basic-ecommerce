package com.azad.basicecommerce.common.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    private String resource;
    private String resourceIdentifier;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, String resource, String resourceIdentifier) {
        super(message);
        this.resource = resource;
        this.resourceIdentifier = resourceIdentifier;
    }
}

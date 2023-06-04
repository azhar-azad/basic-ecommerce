package com.azad.basicecommerce.model.review;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Review extends GenericApiModel {

    @NotNull(message = "Review text cannot be empty")
    protected String reviewText;
}

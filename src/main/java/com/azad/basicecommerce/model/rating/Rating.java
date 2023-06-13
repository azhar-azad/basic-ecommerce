package com.azad.basicecommerce.model.rating;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import com.azad.basicecommerce.common.validators.EnumValidator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Rating extends GenericApiModel {

    @NotNull(message = "Rating value cannot be null")
    @Min(1)
    @Max(5)
    protected Integer ratingValue;
}
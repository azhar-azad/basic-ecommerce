package com.azad.basicecommerce.model.category;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Category extends GenericApiModel {

    @NotNull(message = "Category name must be provided")
    protected String categoryName;
}

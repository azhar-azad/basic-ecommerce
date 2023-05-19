package com.azad.basicecommerce.model.store;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Store extends GenericApiModel {

    @NotNull(message = "Store name cannot be empty")
    protected String storeName;
    protected String pictureUrl;
    protected Double discount;
}

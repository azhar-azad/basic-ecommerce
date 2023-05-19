package com.azad.basicecommerce.model.warehouse;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Warehouse extends GenericApiModel {

    @NotNull(message = "Warehouse name cannot be empty")
    protected String warehouseName;
}

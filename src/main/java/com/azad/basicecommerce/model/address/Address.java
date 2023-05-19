package com.azad.basicecommerce.model.address;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import com.azad.basicecommerce.common.validators.EnumValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Address extends GenericApiModel {

    @NotNull(message = "Address type cannot be empty")
    @EnumValidator(enumClass = AddressTypes.class, message = "Address type is not valid")
    protected String addressType;

    protected String apartment;
    protected String house;
    protected String street;

    @NotNull(message = "Sub-district name cannot be empty")
    protected String subDistrict;

    @NotNull(message = "District name cannot be empty")
    protected String district;

    @NotNull(message = "Division name cannot be empty")
    protected String division;
}

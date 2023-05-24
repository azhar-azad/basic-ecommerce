package com.azad.basicecommerce.model.warehouse;

import com.azad.basicecommerce.model.address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseRequest extends Warehouse {

    private String storeUid;
    private Address address;
}

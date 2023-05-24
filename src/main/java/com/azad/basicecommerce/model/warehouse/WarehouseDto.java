package com.azad.basicecommerce.model.warehouse;

import com.azad.basicecommerce.model.address.Address;
import com.azad.basicecommerce.model.store.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseDto extends Warehouse {

    private Long id;
    private String uid;
    private String storeUid;
    private Store store;
    private Address address;
}

package com.azad.basicecommerce.model.store;

import com.azad.basicecommerce.model.auth.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StoreResponse extends Store {

    private AppUser storeOwner;
}

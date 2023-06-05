package com.azad.basicecommerce.model.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductResponse extends Product {

    private String categoryUid;
    private String storeUid;
}

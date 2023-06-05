package com.azad.basicecommerce.model.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto extends Product {

    private Long id;
    private String categoryUid;
    private String storeUid;
}

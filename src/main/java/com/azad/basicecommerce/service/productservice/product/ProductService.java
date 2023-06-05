package com.azad.basicecommerce.service.productservice.product;

import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiService;
import com.azad.basicecommerce.model.product.ProductDto;

import java.util.List;

public interface ProductService extends GenericApiService<ProductDto> {

    List<ProductDto> getAllByUid(String uid, PagingAndSorting ps);
}

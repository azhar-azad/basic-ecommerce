package com.azad.basicecommerce.service.productservice.rating;

import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiService;
import com.azad.basicecommerce.model.rating.RatingDto;

import java.util.List;

public interface RatingService extends GenericApiService<RatingDto> {

    List<RatingDto> getAllByProduct(String productUid, PagingAndSorting ps);
}

package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.product.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByUid(String uid);
    Optional<List<ProductEntity>> findByStoreUid(String storeUid, Pageable pageable);
    Optional<List<ProductEntity>> findByCategoryUid(String categoryUid, Pageable pageable);
}

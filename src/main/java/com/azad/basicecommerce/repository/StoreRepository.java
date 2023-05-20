package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.store.StoreEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends PagingAndSortingRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByUid(String uid);
    Optional<List<StoreEntity>> findByStoreOwnerId(Long storeOwnerId, Pageable pageable);
}

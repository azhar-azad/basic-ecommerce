package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.warehouse.WarehouseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends PagingAndSortingRepository<WarehouseEntity, Long> {

    Optional<WarehouseEntity> findByUid(String uid);

    Optional<List<WarehouseEntity>> findByStoreUid(String storeUid, Pageable pageable);
}

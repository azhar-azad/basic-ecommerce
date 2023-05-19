package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.address.AddressEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {

    List<AddressEntity> findByUserId(Long userId);
//    AddressEntity findByWarehouseId(Long warehouseId);
}

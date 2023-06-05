package com.azad.basicecommerce.service.inventoryservice.warehouse;

import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiService;
import com.azad.basicecommerce.model.warehouse.WarehouseDto;

import java.util.List;

public interface WarehouseService extends GenericApiService<WarehouseDto> {

    List<WarehouseDto> getAllByStore(String storeUid, PagingAndSorting ps);
}

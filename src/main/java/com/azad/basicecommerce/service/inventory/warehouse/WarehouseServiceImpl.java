package com.azad.basicecommerce.service.inventory.warehouse;

import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.exceptions.ResourceNotFoundException;
import com.azad.basicecommerce.common.exceptions.UnauthorizedAccessException;
import com.azad.basicecommerce.model.address.Address;
import com.azad.basicecommerce.model.address.AddressEntity;
import com.azad.basicecommerce.model.auth.AppUserEntity;
import com.azad.basicecommerce.model.store.Store;
import com.azad.basicecommerce.model.store.StoreEntity;
import com.azad.basicecommerce.model.warehouse.WarehouseDto;
import com.azad.basicecommerce.model.warehouse.WarehouseEntity;
import com.azad.basicecommerce.repository.AddressRepository;
import com.azad.basicecommerce.repository.StoreRepository;
import com.azad.basicecommerce.repository.WarehouseRepository;
import com.azad.basicecommerce.security.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    private final WarehouseRepository repository;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository repository) {
        this.repository = repository;
    }

    @Override
    public WarehouseDto create(WarehouseDto dto) {

        apiUtils.logInfo("*** WAREHOUSE :: CREATE ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        String roleName = loggedInUser.getRole().getRoleName();
        if (!roleName.equalsIgnoreCase("SELLER") && !roleName.equalsIgnoreCase("ADMIN")) {
            apiUtils.logError("*** Only SELLER or ADMIN can create a new Warehouse ***");
        }

        StoreEntity store = storeRepository.findByUid(dto.getStoreUid()).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "WAREHOUSE", "uid = " + dto.getStoreUid()));

        if (loggedInUserIsNotOwner(store.getStoreOwner().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** Logged in user does not have authorization to add warehouse ***");
            throw new UnauthorizedAccessException("Unauthorized Access", loggedInUser.getRole().getRoleName(), "SELLER, ADMIN");
        }

        WarehouseEntity entity = modelMapper.map(dto, WarehouseEntity.class);
        entity.setUid(apiUtils.getHash("warehouse", dto.getWarehouseName() + store.getStoreName()));
        entity.setStore(store);
        entity.setAddress(null);

        WarehouseEntity savedEntity = repository.save(entity);

        WarehouseDto savedDto = modelMapper.map(savedEntity, WarehouseDto.class);
        savedDto.setStore(modelMapper.map(store, Store.class));
        savedDto.setStoreUid(store.getUid());

        if (dto.getAddress() != null) {
            dto.getAddress().setAddressType(dto.getAddress().getAddressType().trim().toUpperCase());
            AddressEntity savedAddress = saveAddress(dto.getAddress(), savedEntity);
            savedDto.setAddress(modelMapper.map(savedAddress, Address.class));
        }

        return savedDto;
    }

    @Override
    public List<WarehouseDto> getAll(PagingAndSorting ps) {
        return null;
    }

    @Override
    public WarehouseDto getById(Long id) {
        return null;
    }

    @Override
    public WarehouseDto getByUid(String uid) {
        return null;
    }

    @Override
    public WarehouseDto updateById(Long id, WarehouseDto updatedDto) {
        return null;
    }

    @Override
    public WarehouseDto updateByUid(String uid, WarehouseDto updatedDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByUid(String uid) {

    }

    @Override
    public Long getEntityCount() {
        return null;
    }

    private boolean loggedInUserIsNotOwner(Long dbEntityUserId, Long storeOwnerId) {

        return !Objects.equals(dbEntityUserId, storeOwnerId);
    }

    private AddressEntity saveAddress(Address address, WarehouseEntity warehouse) {
        AddressEntity entity = modelMapper.map(address, AddressEntity.class);
        entity.setUid(apiUtils.getHash("address",
                address.getAddressType() + address.getApartment() + address.getHouse()
                        + address.getSubDistrict() + address.getDistrict()));
        entity.setWarehouse(warehouse);

        return addressRepository.save(entity);
    }
}

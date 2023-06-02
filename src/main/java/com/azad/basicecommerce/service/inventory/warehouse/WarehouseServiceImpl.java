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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        entity.setUid(apiUtils.generateWarehouseUid(entity.getWarehouseName(), store.getStoreName()));
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

        apiUtils.logInfo("*** WAREHOUSE :: GET ALL BY USER ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        List<Long> storeIds = loggedInUser.getStores().stream()
                .map(StoreEntity::getId).collect(Collectors.toList());
        List<WarehouseEntity> entitiesFromDb = new ArrayList<>();
        for (Long storeId: storeIds) {
            Optional<List<WarehouseEntity>> byStoreId = repository.findByStoreId(storeId, apiUtils.getPageable(ps));
            byStoreId.ifPresent(entitiesFromDb::addAll);
        }
        if (entitiesFromDb.size() == 0)
            return null;

        return entitiesFromDb.stream()
                .map(entity -> {
                    WarehouseDto dto = modelMapper.map(entity, WarehouseDto.class);
                    dto.setAddress(modelMapper.map(entity.getAddress(), Address.class));
                    dto.setStore(modelMapper.map(entity.getStore(), Store.class));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<WarehouseDto> getAllByStore(String storeUid, PagingAndSorting ps) {

        apiUtils.logInfo("*** WAREHOUSE :: GET ALL BY STORE ***");

        StoreEntity store = storeRepository.findByUid(storeUid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "WAREHOUSE", "uid = " + storeUid));

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        if (loggedInUserIsNotOwner(store.getStoreOwner().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** Logged in user does not have authorization to get this warehouse data ***");
            throw new UnauthorizedAccessException("Unauthorized Access", loggedInUser.getRole().getRoleName(), "SELLER, ADMIN");
        }

        Optional<List<WarehouseEntity>> byStoreId = repository.findByStoreId(store.getId(), apiUtils.getPageable(ps));

        return byStoreId.map(warehouseEntities -> warehouseEntities.stream()
                .map(entity -> {
                    WarehouseDto dto = modelMapper.map(entity, WarehouseDto.class);
                    dto.setAddress(modelMapper.map(entity.getAddress(), Address.class));
                    dto.setStore(modelMapper.map(entity.getStore(), Store.class));
                    return dto;
                }).collect(Collectors.toList())).orElse(null);

    }

    @Override
    public WarehouseDto getById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public WarehouseDto getByUid(String uid) {

        apiUtils.logInfo("*** WAREHOUSE :: GET BY UID ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();

        WarehouseEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "WAREHOUSE", "uid = " + uid));

        if (!loggedInUser.getStores().contains(entity.getStore())) {
            return null;
        }

        WarehouseDto dto = modelMapper.map(entity, WarehouseDto.class);
        dto.setAddress(modelMapper.map(entity.getAddress(), Address.class));
        dto.setStore(modelMapper.map(entity.getStore(), Store.class));

        return dto;
    }

    @Override
    public WarehouseDto updateById(Long id, WarehouseDto updatedDto) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public WarehouseDto updateByUid(String uid, WarehouseDto updatedDto) {

        apiUtils.logInfo("*** WAREHOUSE :: UPDATE BY UID ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();

        WarehouseEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "WAREHOUSE", "uid = " + uid));

        if (!loggedInUser.getStores().contains(entity.getStore())) {
            apiUtils.logError("*** Logged in user does not have authorization to update this warehouse data ***");
            throw new UnauthorizedAccessException("Unauthorized Access", loggedInUser.getRole().getRoleName(), "SELLER, ADMIN");
        }

        if (updatedDto.getWarehouseName() != null)
            entity.setWarehouseName(updatedDto.getWarehouseName());
        entity.setUid(apiUtils.generateWarehouseUid(entity.getWarehouseName(), entity.getStore().getStoreName()));

        WarehouseEntity updatedEntity = repository.save(entity);
        WarehouseDto savedDto = modelMapper.map(updatedEntity, WarehouseDto.class);

        if (updatedDto.getAddress() != null) {
            updatedDto.getAddress().setAddressType(updatedDto.getAddress().getAddressType().trim().toUpperCase());
            AddressEntity savedAddress = saveAddress(updatedDto.getAddress(), updatedEntity);
            savedDto.setAddress(modelMapper.map(savedAddress, Address.class));
        }

        return savedDto;
    }

    @Override
    public void deleteById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public void deleteByUid(String uid) {

        apiUtils.logInfo("*** WAREHOUSE :: DELETE BY UID ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();

        WarehouseEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "WAREHOUSE", "uid = " + uid));

        if (!loggedInUser.getStores().contains(entity.getStore())) {
            apiUtils.logError("*** Logged in user does not have authorization to delete this warehouse data ***");
            throw new UnauthorizedAccessException("Unauthorized Access", loggedInUser.getRole().getRoleName(), "SELLER, ADMIN");
        }

        repository.delete(entity);
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
        entity.setUid(apiUtils.generateAddressUid(address.getAddressType(), address.getApartment(),
                address.getHouse(), address.getSubDistrict(), address.getDistrict()));
        entity.setWarehouse(warehouse);

        return addressRepository.save(entity);
    }
}

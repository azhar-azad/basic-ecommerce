package com.azad.basicecommerce.service.store;

import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.exceptions.ResourceNotFoundException;
import com.azad.basicecommerce.common.exceptions.UnauthorizedAccessException;
import com.azad.basicecommerce.model.auth.AppUser;
import com.azad.basicecommerce.model.auth.AppUserEntity;
import com.azad.basicecommerce.model.auth.RoleEntity;
import com.azad.basicecommerce.model.store.StoreDto;
import com.azad.basicecommerce.model.store.StoreEntity;
import com.azad.basicecommerce.repository.AppUserRepository;
import com.azad.basicecommerce.repository.RoleRepository;
import com.azad.basicecommerce.repository.StoreRepository;
import com.azad.basicecommerce.security.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final StoreRepository repository;

    @Autowired
    public StoreServiceImpl(StoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public StoreDto create(StoreDto dto) {

        apiUtils.logInfo("*** STORE :: CREATE ***");

        AppUserEntity storeOwner = authService.getLoggedInUser();

        if (storeOwner.getRole().getRoleName().equalsIgnoreCase("USER")) {
            // Update storeOwner's role to 'SELLER'
            Optional<RoleEntity> byRoleName = roleRepository.findByRoleName("SELLER");
            if (!byRoleName.isPresent())
                throw new ResourceNotFoundException("Invalid Role Name", "ROLE", "roleName = SELLER");
            storeOwner.setRole(byRoleName.get());
            storeOwner = appUserRepository.save(storeOwner);
        }

        if (dto.getDiscount() == null)
            dto.setDiscount(0.0);

        StoreEntity entityFromReq = modelMapper.map(dto, StoreEntity.class);
        entityFromReq.setUid(apiUtils.getHash("store", dto.getStoreName() + storeOwner.getEmail()));
        entityFromReq.setStoreOwner(storeOwner);

        StoreEntity savedEntity = repository.save(entityFromReq);

        StoreDto savedDto = modelMapper.map(savedEntity, StoreDto.class);
        savedDto.setStoreOwner(modelMapper.map(storeOwner, AppUser.class));

        return savedDto;
    }

    @Override
    public List<StoreDto> getAll(PagingAndSorting ps) {

        apiUtils.logInfo("*** STORE :: GET ALL ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();

        Optional<List<StoreEntity>> byStoreOwnerId =
                repository.findByStoreOwnerId(loggedInUser.getId(), apiUtils.getPageable(ps));

        return byStoreOwnerId.map(storeEntities -> storeEntities.stream()
                .map(entity -> {
                    StoreDto dto = modelMapper.map(entity, StoreDto.class);
                    dto.setStoreOwner(modelMapper.map(loggedInUser, AppUser.class));
                    return dto;
                })
                .collect(Collectors.toList())).orElse(null);
    }

    @Override
    public StoreDto getById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public StoreDto getByUid(String uid) {

        apiUtils.logInfo("*** STORE :: GET ***");

        StoreEntity entityFromDb = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Store UID", "STORE", "uid = " + uid));

        String storeOwnerUid = entityFromDb.getStoreOwner().getUid();
        Optional<AppUserEntity> byUserUid = appUserRepository.findByUid(storeOwnerUid);
        if (!byUserUid.isPresent())
            throw new ResourceNotFoundException("Invalid User UID", "USER", "uid = " + storeOwnerUid);

        StoreDto fetchedDto = modelMapper.map(entityFromDb, StoreDto.class);
        fetchedDto.setStoreOwner(modelMapper.map(byUserUid.get(), AppUser.class));

        return fetchedDto;
    }

    @Override
    public StoreDto updateById(Long id, StoreDto updatedDto) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public StoreDto updateByUid(String uid, StoreDto updatedDto) {

        apiUtils.logInfo("*** STORE :: UPDATE ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();

        StoreEntity entityFromDb = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Store UID", "STORE", "uid = " + uid));

        if (loggedInUserIsNotOwner(entityFromDb.getStoreOwner().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** ERROR :: Logged in user is not the owner ***");
            throw new UnauthorizedAccessException("Unauthorized. Logged in user is not the owner of this store");
        }

        if (updatedDto.getStoreName() != null)
            entityFromDb.setStoreName(updatedDto.getStoreName());
        if (updatedDto.getPictureUrl() != null)
            entityFromDb.setPictureUrl(updatedDto.getPictureUrl());
        if (updatedDto.getDiscount() != null)
            entityFromDb.setDiscount(updatedDto.getDiscount());
        entityFromDb.setUid(apiUtils.getHash("store", entityFromDb.getStoreName() + entityFromDb.getStoreOwner().getEmail()));

        StoreDto savedDto = modelMapper.map(repository.save(entityFromDb), StoreDto.class);
        savedDto.setStoreOwner(modelMapper.map(loggedInUser, AppUser.class));

        return savedDto;
    }

    @Override
    public void deleteById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public void deleteByUid(String uid) {

        apiUtils.logInfo("*** STORE :: DELETE ***");

        AppUserEntity storeOwner = authService.getLoggedInUser();

        StoreEntity entityFromDb = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "STORE", "uid = " + uid));

        if (loggedInUserIsNotOwner(entityFromDb.getStoreOwner().getId(), storeOwner.getId())) {
            throw new UnauthorizedAccessException("Unauthorized. Logged in user is not the owner of this store");
        }

        repository.delete(entityFromDb);
    }

    @Override
    public Long getEntityCount() {
        return repository.count();
    }

    private boolean loggedInUserIsNotOwner(Long dbEntityUserId, Long storeOwnerId) {

        return !Objects.equals(dbEntityUserId, storeOwnerId);
    }
}

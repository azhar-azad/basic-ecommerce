package com.azad.basicecommerce.service.productservice.product;

import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.exceptions.ResourceNotFoundException;
import com.azad.basicecommerce.common.exceptions.UnauthorizedAccessException;
import com.azad.basicecommerce.model.auth.AppUserEntity;
import com.azad.basicecommerce.model.category.CategoryEntity;
import com.azad.basicecommerce.model.product.ProductDto;
import com.azad.basicecommerce.model.product.ProductEntity;
import com.azad.basicecommerce.model.store.StoreEntity;
import com.azad.basicecommerce.repository.CategoryRepository;
import com.azad.basicecommerce.repository.ProductRepository;
import com.azad.basicecommerce.repository.StoreRepository;
import com.azad.basicecommerce.security.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDto create(ProductDto dto) {

        apiUtils.logInfo("*** PRODUCT :: CREATE ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        String roleName = loggedInUser.getRole().getRoleName();
        if (!roleName.equalsIgnoreCase("SELLER")) {
            apiUtils.logError("*** Only SELLER can create a new Product ***");
            throw new UnauthorizedAccessException("Unauthorized Access", roleName, "SELLER");
        }

        StoreEntity store = storeRepository.findByUid(dto.getStoreUid()).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "STORE", "uid = " + dto.getStoreUid()));

        if (loggedInUserIsNotOwner(store.getStoreOwner().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** Logged in user does not have authorization to add a product in this store ***");
            throw new UnauthorizedAccessException("Unauthorized Access", roleName, "SELLER");
        }

        CategoryEntity category = categoryRepository.findByUid(dto.getCategoryUid()).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "CATEGORY", "uid = " + dto.getCategoryUid()));

        ProductEntity entity = modelMapper.map(dto, ProductEntity.class);
        entity.setUid(apiUtils.generateProductUid(entity.getProductName(), entity.getBrand(), String.valueOf(entity.getPrice()), store.getStoreName()));
        entity.setStore(store);
        entity.setCategory(category);
        entity.setAverageRating(0.0);
        entity.setTotalRating(0L);
        entity.setTotalReview(0L);

        ProductEntity savedEntity = repository.save(entity);

        ProductDto savedDto = modelMapper.map(savedEntity, ProductDto.class);
        savedDto.setStoreUid(savedEntity.getStore().getUid());
        savedDto.setCategoryUid(savedEntity.getCategory().getUid());

        return savedDto;
    }

    @Override
    public List<ProductDto> getAll(PagingAndSorting ps) {

        apiUtils.logInfo("*** PRODUCT :: GET ALL ***");

        List<ProductEntity> entitiesFromDb = repository.findAll(apiUtils.getPageable(ps)).getContent();

        return convertAndReturnEntitiesToDtos(entitiesFromDb);
    }

    @Override
    public List<ProductDto> getAllByUid(String uid, PagingAndSorting ps) {

        apiUtils.logInfo("*** PRODUCT :: GET ALL BY STORE/CATEGORY ***");

        List<ProductEntity> entitiesFromDb;

        if (uid.startsWith("store")) {
            Optional<List<ProductEntity>> byStoreUid = repository.findByStoreUid(uid, apiUtils.getPageable(ps));
            if (!byStoreUid.isPresent())
                return null;
            entitiesFromDb = byStoreUid.get();
        }
        else if (uid.startsWith("category")) {
            Optional<List<ProductEntity>> byCategoryUid = repository.findByCategoryUid(uid, apiUtils.getPageable(ps));
            if (!byCategoryUid.isPresent())
                return null;
            entitiesFromDb = byCategoryUid.get();
        }
        else {
            apiUtils.logError("*** The uid is neither of store nor category. Passed uid = " + uid + " ***");
            throw new RuntimeException("Invalid UID is passed");
        }

        return convertAndReturnEntitiesToDtos(entitiesFromDb);
    }

    @Override
    public ProductDto getById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public ProductDto getByUid(String uid) {

        apiUtils.logInfo("*** PRODUCT :: GET BY UID ***");

        ProductEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "PRODUCT", "uid = " + uid));

        ProductDto dto = modelMapper.map(entity, ProductDto.class);
        dto.setStoreUid(entity.getStore().getUid());
        dto.setCategoryUid(entity.getCategory().getUid());

        return dto;
    }

    @Override
    public ProductDto updateById(Long id, ProductDto updatedDto) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public ProductDto updateByUid(String uid, ProductDto updatedDto) {

        apiUtils.logInfo("*** PRODUCT :: UPDATE BY UID ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        String roleName = loggedInUser.getRole().getRoleName();
        if (!roleName.equalsIgnoreCase("SELLER")) {
            apiUtils.logError("*** Only SELLER can update his/her Product ***");
            throw new UnauthorizedAccessException("Unauthorized Access", roleName, "SELLER");
        }

        ProductEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "PRODUCT", "uid = " + uid));

        if (loggedInUserIsNotOwner(entity.getStore().getStoreOwner().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** Logged in user does not have authorization to update a product in this store ***");
            throw new UnauthorizedAccessException("Unauthorized Access", roleName, "SELLER");
        }

        if (updatedDto.getProductName() != null)
            entity.setProductName(updatedDto.getProductName());
        if (updatedDto.getBrand() != null)
            entity.setBrand(updatedDto.getBrand());
        if (updatedDto.getDescription() != null)
            entity.setDescription(updatedDto.getDescription());
        if (updatedDto.getPictureUrl() != null)
            entity.setPictureUrl(updatedDto.getPictureUrl());
        if (updatedDto.getPrice() != null)
            entity.setPrice(updatedDto.getPrice());
        if (updatedDto.getEstDeliveryTimeInDays() != null)
            entity.setEstDeliveryTimeInDays(updatedDto.getEstDeliveryTimeInDays());
        if (updatedDto.getReturnPolicy() != null)
            entity.setReturnPolicy(updatedDto.getReturnPolicy());
        if (updatedDto.getAvailableInStock() != null)
            entity.setAvailableInStock(updatedDto.getAvailableInStock());
        if (updatedDto.getLowStockThreshold() != null)
            entity.setLowStockThreshold(updatedDto.getLowStockThreshold());
        if (updatedDto.getDiscount() != null)
            entity.setDiscount(updatedDto.getDiscount());
        if (updatedDto.getWarranty() != null)
            entity.setWarranty(updatedDto.getWarranty());
        entity.setUid(apiUtils.generateProductUid(entity.getProductName(), entity.getBrand(),
                String.valueOf(entity.getPrice()), entity.getStore().getStoreName()));

        ProductEntity updatedEntity = repository.save(entity);

        ProductDto updatedAndSavedDto = modelMapper.map(updatedEntity, ProductDto.class);
        updatedAndSavedDto.setStoreUid(updatedEntity.getStore().getUid());
        updatedAndSavedDto.setCategoryUid(updatedEntity.getCategory().getUid());

        return updatedAndSavedDto;
    }

    @Override
    public void deleteById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public void deleteByUid(String uid) {

        apiUtils.logInfo("*** PRODUCT :: DELETE BY UID ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        String roleName = loggedInUser.getRole().getRoleName();
        if (!roleName.equalsIgnoreCase("SELLER")) {
            apiUtils.logError("*** Only SELLER can delete his/her Product ***");
            throw new UnauthorizedAccessException("Unauthorized Access", roleName, "SELLER");
        }

        ProductEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "PRODUCT", "uid = " + uid));

        if (loggedInUserIsNotOwner(entity.getStore().getStoreOwner().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** Logged in user does not have authorization to delete a product in this store ***");
            throw new UnauthorizedAccessException("Unauthorized Access", roleName, "SELLER");
        }

        repository.delete(entity);
    }

    @Override
    public Long getEntityCount() {
        return repository.count();
    }

    private boolean loggedInUserIsNotOwner(Long dbEntityUserId, Long storeOwnerId) {
        return !Objects.equals(dbEntityUserId, storeOwnerId);
    }

    private List<ProductDto> convertAndReturnEntitiesToDtos(List<ProductEntity> entities) {

        if (entities == null || entities.size() == 0)
            return null;

        return entities.stream()
                .map(entity -> {
                    ProductDto dto = modelMapper.map(entity, ProductDto.class);
                    dto.setStoreUid(entity.getStore().getUid());
                    dto.setCategoryUid(entity.getCategory().getUid());
                    return dto;
                }).collect(Collectors.toList());
    }
}

package com.azad.basicecommerce.service.productservice.rating;

import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.exceptions.ResourceNotFoundException;
import com.azad.basicecommerce.common.exceptions.UnauthorizedAccessException;
import com.azad.basicecommerce.model.auth.AppUserEntity;
import com.azad.basicecommerce.model.product.ProductEntity;
import com.azad.basicecommerce.model.rating.RatingDto;
import com.azad.basicecommerce.model.rating.RatingEntity;
import com.azad.basicecommerce.repository.ProductRepository;
import com.azad.basicecommerce.repository.RatingRepository;
import com.azad.basicecommerce.security.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    private ProductRepository productRepository;

    private final RatingRepository repository;

    @Autowired
    public RatingServiceImpl(RatingRepository repository) {
        this.repository = repository;
    }

    @Override
    public RatingDto create(RatingDto dto) {

        apiUtils.logInfo("*** RATING :: CREATE ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        if (!loggedInUser.getRole().getRoleName().equalsIgnoreCase("USER")) {
            apiUtils.logError("*** Only USER can give ratings to any product ***");
            throw new UnauthorizedAccessException("Logged in user is not a regular user. Only regular users can give ratings to products");
        }

        ProductEntity product = productRepository.findByUid(dto.getProductUid()).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "PRODUCT", "uid = " + dto.getProductUid()));

        Optional<List<RatingEntity>> byProductUid = repository.findByProductId(product.getId(), null);
        if (byProductUid.isPresent()) {
            List<RatingEntity> ratings = byProductUid.get();
            for (RatingEntity rating: ratings) {
                if (Objects.equals(rating.getUser().getId(), loggedInUser.getId())) {
                    apiUtils.logError("*** This user has already given a rating for this product ***");
                    throw new RuntimeException("Frontend should not allow this user to provide another rating as this user has already given a rating for this product");
                }
            }
        }

        product.setAverageRating((product.getTotalRating() * product.getAverageRating() + dto.getRatingValue()) / (product.getTotalRating() + 1));
        product.setTotalRating(product.getTotalRating() + 1);
        product = productRepository.save(product);

        RatingEntity entity = modelMapper.map(dto, RatingEntity.class);
        entity.setProduct(product);
        entity.setUser(loggedInUser);
        entity.setUid(apiUtils.generateRatingUid(String.valueOf(entity.getRatingValue()), product.getUid(), loggedInUser.getUid()));

        RatingEntity savedEntity = repository.save(entity);

        RatingDto savedDto = modelMapper.map(savedEntity, RatingDto.class);
        savedDto.setProductUid(savedEntity.getProduct().getUid());
        savedDto.setUserUid(savedEntity.getUser().getUid());

        return savedDto;
    }

    @Override
    public List<RatingDto> getAll(PagingAndSorting ps) {

        apiUtils.logInfo("*** RATING :: GET ALL BY USER ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();

        Optional<List<RatingEntity>> byUserId = repository.findByUserId(loggedInUser.getId(), apiUtils.getPageable(ps));
        if (!byUserId.isPresent()) {
            apiUtils.logInfo("*** Logged in user did not give any rating yet ***");
            return null;
        }

        return convertEntitiesToDtosAndReturn(byUserId.get());
    }

    @Override
    public List<RatingDto> getAllByProduct(String productUid, PagingAndSorting ps) {

        apiUtils.logInfo("*** RATING :: GET ALL BY PRODUCT ***");

        Long productId = productRepository.findByUid(productUid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "PRODUCT", "uid = " + productUid)).getId();

        Optional<List<RatingEntity>> byProductId = repository.findByProductId(productId, apiUtils.getPageable(ps));
        if (!byProductId.isPresent()) {
            apiUtils.logInfo("*** There is no rating for this product ***");
            return null;
        }

        return convertEntitiesToDtosAndReturn(byProductId.get());
    }

    @Override
    public RatingDto getById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public RatingDto getByUid(String uid) {

        apiUtils.logInfo("*** RATING :: GET BY UID ***");

        RatingEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "RATING", "uid = " + uid));

        RatingDto dto = modelMapper.map(entity, RatingDto.class);
        dto.setProductUid(entity.getProduct().getUid());
        dto.setUserUid(entity.getUser().getUid());

        return dto;
    }

    @Override
    public RatingDto updateById(Long id, RatingDto updatedDto) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public RatingDto updateByUid(String uid, RatingDto updatedDto) {

        apiUtils.logInfo("*** RATING :: UPDATE BY UID ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        String roleName = loggedInUser.getRole().getRoleName();
        if (!roleName.equalsIgnoreCase("USER")) {
            apiUtils.logError("*** Only USER can give ratings to any product ***");
            throw new UnauthorizedAccessException("Logged in user is not a regular user. Only regular users can update ratings to products");
        }

        RatingEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "RATING", "uid = " + uid));

        if (Objects.equals(entity.getUser().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** Logged in user is not the one that gave this rating ***");
            throw new UnauthorizedAccessException("Logged in user is not the owner of this rating");
        }

        if (updatedDto.getRatingValue() != null)
            entity.setRatingValue(updatedDto.getRatingValue());
        entity.setUid(apiUtils.generateRatingUid(String.valueOf(entity.getRatingValue()), entity.getProduct().getUid(), entity.getUser().getUid()));

        RatingEntity updatedEntity = repository.save(entity);

        RatingDto dto = modelMapper.map(updatedEntity, RatingDto.class);
        dto.setProductUid(updatedEntity.getProduct().getUid());
        dto.setUserUid(updatedEntity.getUser().getUid());

        return dto;
    }

    @Override
    public void deleteById(Long id) {
        throw new RuntimeException("Method should not be used.");
    }

    @Override
    public void deleteByUid(String uid) {

        apiUtils.logInfo("*** RATING :: DELETE BY UID ***");

        AppUserEntity loggedInUser = authService.getLoggedInUser();
        String roleName = loggedInUser.getRole().getRoleName();
        if (!roleName.equalsIgnoreCase("USER")) {
            apiUtils.logError("*** Only USER can give ratings to any product ***");
            throw new UnauthorizedAccessException("Logged in user is not a regular user. Only regular users can delete ratings to products");
        }

        RatingEntity entity = repository.findByUid(uid).orElseThrow(
                () -> new ResourceNotFoundException("Invalid UID", "RATING", "uid = " + uid));

        if (Objects.equals(entity.getUser().getId(), loggedInUser.getId())) {
            apiUtils.logError("*** Logged in user is not the one that gave this rating ***");
            throw new UnauthorizedAccessException("Logged in user is not the owner of this rating");
        }

        repository.delete(entity);
    }

    @Override
    public Long getEntityCount() {
        return repository.count();
    }

    private List<RatingDto> convertEntitiesToDtosAndReturn(List<RatingEntity> ratingEntities) {

        return ratingEntities.stream()
                .map(entity -> {
                    RatingDto dto = modelMapper.map(entity, RatingDto.class);
                    dto.setUserUid(entity.getUser().getUid());
                    dto.setProductUid(entity.getProduct().getUid());
                    return dto;
                }).collect(Collectors.toList());
    }
}

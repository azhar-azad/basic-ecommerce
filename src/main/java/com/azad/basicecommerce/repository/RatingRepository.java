package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.rating.RatingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<RatingEntity, Long> {

    Optional<RatingEntity> findByUid(String uid);
    Optional<List<RatingEntity>> findByProductId(Long productId, Pageable pageable);
    Optional<List<RatingEntity>> findByUserId(Long userId, Pageable pageable);
}

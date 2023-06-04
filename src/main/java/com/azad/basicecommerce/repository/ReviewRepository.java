package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.review.ReviewEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByUid(String uid);
    Optional<List<ReviewEntity>> findByProductUid(String productUid, Pageable pageable);
    Optional<List<ReviewEntity>> findByReviewerUid(String reviewerUid, Pageable pageable);
}

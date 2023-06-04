package com.azad.basicecommerce.model.review;

import com.azad.basicecommerce.model.auth.AppUserEntity;
import com.azad.basicecommerce.model.product.ProductEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_uid", nullable = false, unique = true)
    private String uid;   // reviewText + productUid + userUid

    @Column(name = "review_text", nullable = false)
    private String reviewText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private AppUserEntity reviewer;
}

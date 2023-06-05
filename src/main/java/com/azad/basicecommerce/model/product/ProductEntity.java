package com.azad.basicecommerce.model.product;

import com.azad.basicecommerce.model.category.CategoryEntity;
import com.azad.basicecommerce.model.rating.RatingEntity;
import com.azad.basicecommerce.model.review.ReviewEntity;
import com.azad.basicecommerce.model.store.StoreEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_uid", nullable = false, unique = true)
    private String uid;     // productName + brand + price + storeName

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "estimated_delivery_time", nullable = false)
    private Integer estDeliveryTimeInDays;

    @Column(name = "return_policy", nullable = false)
    private String returnPolicy;

    @Column(name = "available_in_stock", nullable = false)
    private Long availableInStock;

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "warranty")
    private String warranty;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "total_rating")
    private Long totalRating;

    @Column(name = "total_review")
    private Long totalReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RatingEntity> ratings;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews;
}

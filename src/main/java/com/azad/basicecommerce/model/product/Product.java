package com.azad.basicecommerce.model.product;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Product extends GenericApiModel {

    @NotNull(message = "Product name cannot be empty")
    protected String productName;

    @NotNull(message = "Product brand cannot be empty")
    protected String brand;

    @NotNull(message = "Product description cannot be empty")
    protected String description;

    @NotNull(message = "Picture URL cannot be empty")
    protected String pictureUrl;

    @NotNull(message = "Product price cannot be empty")
    protected Double price;

    @NotNull(message = "Product's estimated delivery time(days) cannot be empty")
    protected Integer estDeliveryTimeInDays;

    @NotNull(message = "Product's return policy cannot be empty")
    protected String returnPolicy;

    @NotNull(message = "Available product quantity in stock cannot be empty")
    protected Long availableInStock;

    @NotNull(message = "Low stock threshold for a product cannot be empty")
    protected Integer lowStockThreshold;

    protected Double discount;
    protected String warranty;
    protected Double averageRating;
    protected Long totalRating;
    protected Long totalReview;
}

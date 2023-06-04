package com.azad.basicecommerce.model.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewResponse extends Review {

    private String productUid;
    private String reviewerUid;
}

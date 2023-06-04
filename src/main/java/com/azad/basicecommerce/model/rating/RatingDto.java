package com.azad.basicecommerce.model.rating;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RatingDto extends Rating {

    private Long id;
    private String productUid;
    private String userUid;
}

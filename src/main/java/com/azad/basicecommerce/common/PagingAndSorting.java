package com.azad.basicecommerce.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PagingAndSorting {

    private int page;
    private int limit;
    private String sort;
    private String order;
}

package com.azad.basicecommerce.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PagingAndSorting {

    private int page;
    private int limit;
    private String sort;
    private String order;
}

package com.azad.basicecommerce.common.generics;

import com.azad.basicecommerce.common.PagingAndSorting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/***
 * Generic response model assembler.
 *      toModel:
 *          - Receive a response class.
 *          - Convert to EntityModel and return.
 *      toCollectionModel:
 *          - Receive an Iterable of responses.
 *          - Convert each response to EntityModel and add to a CollectionModel.
 *          - Return the CollectionModel.
 *      getCollectionModel:
 *          - Receive an Iterable of responses and the paging and sorting data.
 *          - Convert to CollectionModel using toCollectionModel method.
 *          - Add PREV and NEXT links using the addNextLink and addPrevLink methods. (if applicable)
 *          - Return the CollectionModel.
 *      addNextLink:
 *          - Receive a CollectionModel and paging and sorting data.
 *          - Add NEXT link if applicable.
 *          - Return the CollectionModel.
 *      addPrevLink:
 *          - Receive a CollectionModel and paging and sorting data.
 *          - Add PREV link if applicable.
 *          - Return the CollectionModel.
 *
 * @param <T> Entity response class.
 */
public interface GenericApiResponseModelAssembler<T> extends RepresentationModelAssembler<T, EntityModel<T>> {

    @Value("${default_page_number}")
    int defaultPage = 0;
    @Value("${default_result_limit}")
    int defaultLimit = 25;
    @Value("${default_sort_order}")
    String defaultOrder = "asc";

    EntityModel<T> toModel(T response);
    CollectionModel<EntityModel<T>> toCollectionModel(Iterable<? extends T> responses);
    CollectionModel<EntityModel<T>> getCollectionModel(Iterable<? extends T> responses, PagingAndSorting ps);
    void addNextLink(CollectionModel<EntityModel<T>> collectionModel, PagingAndSorting ps);
    void addPrevLink(CollectionModel<EntityModel<T>> collectionModel, PagingAndSorting ps);
}

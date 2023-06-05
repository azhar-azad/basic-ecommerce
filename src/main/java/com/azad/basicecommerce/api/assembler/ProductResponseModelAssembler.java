package com.azad.basicecommerce.api.assembler;

import com.azad.basicecommerce.api.resource.ProductRestResource;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiResponseModelAssembler;
import com.azad.basicecommerce.model.product.ProductRequest;
import com.azad.basicecommerce.model.product.ProductResponse;
import com.azad.basicecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductResponseModelAssembler implements GenericApiResponseModelAssembler<ProductResponse> {

    @Autowired
    private ProductRepository repository;

    @Override
    public EntityModel<ProductResponse> toModel(ProductResponse response) {
        EntityModel<ProductResponse> responseEntityModel = EntityModel.of(response);

        responseEntityModel.add(linkTo(methodOn(ProductRestResource.class)
                .getEntity(response.getUid()))
                .withSelfRel());
        responseEntityModel.add(linkTo(methodOn(ProductRestResource.class)
                .updateEntity(response.getUid(), new ProductRequest()))
                .withRel("edit"));
        responseEntityModel.add(linkTo(methodOn(ProductRestResource.class)
                .deleteEntity(response.getUid()))
                .withRel("remove"));
        responseEntityModel.add(linkTo(methodOn(ProductRestResource.class)
                .getAllEntities(defaultPage, defaultLimit, "", defaultOrder))
                .withRel(IanaLinkRelations.COLLECTION));

        return responseEntityModel;
    }

    @Override
    public CollectionModel<EntityModel<ProductResponse>> toCollectionModel(Iterable<? extends ProductResponse> responses) {
        List<EntityModel<ProductResponse>> responseEntityModels = new ArrayList<>();

        responses.forEach(response -> responseEntityModels.add(toModel(response)));

        return CollectionModel.of(responseEntityModels);
    }

    @Override
    public CollectionModel<EntityModel<ProductResponse>> getCollectionModel(Iterable<? extends ProductResponse> responses, PagingAndSorting ps) {
        CollectionModel<EntityModel<ProductResponse>> responseCollectionModel = toCollectionModel(responses);

        responseCollectionModel.add(linkTo(methodOn(ProductRestResource.class)
                .getAllEntities(ps.getPage(), ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withSelfRel());

        long totalRecords = repository.count();

        if (ps.getPage() > 1)
            addPrevLink(responseCollectionModel, ps);
        if (totalRecords > (long) ps.getPage() * ps.getLimit())
            addNextLink(responseCollectionModel, ps);

        return responseCollectionModel;
    }

    @Override
    public void addNextLink(CollectionModel<EntityModel<ProductResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(ProductRestResource.class)
                .getAllEntities(ps.getPage() + 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.NEXT));
    }

    @Override
    public void addPrevLink(CollectionModel<EntityModel<ProductResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(ProductRestResource.class)
                .getAllEntities(ps.getPage() - 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.PREV));
    }
}

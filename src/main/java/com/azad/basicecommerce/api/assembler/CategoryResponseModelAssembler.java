package com.azad.basicecommerce.api.assembler;

import com.azad.basicecommerce.api.resource.CategoryRestResource;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiResponseModelAssembler;
import com.azad.basicecommerce.model.category.CategoryRequest;
import com.azad.basicecommerce.model.category.CategoryResponse;
import com.azad.basicecommerce.repository.CategoryRepository;
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
public class CategoryResponseModelAssembler implements GenericApiResponseModelAssembler<CategoryResponse> {

    @Autowired
    private CategoryRepository repository;

    @Override
    public EntityModel<CategoryResponse> toModel(CategoryResponse response) {
        EntityModel<CategoryResponse> responseEntityModel = EntityModel.of(response);

        responseEntityModel.add(linkTo(methodOn(CategoryRestResource.class)
                .getEntity(response.getUid()))
                .withSelfRel());
        responseEntityModel.add(linkTo(methodOn(CategoryRestResource.class)
                .updateEntity(response.getUid(), new CategoryRequest()))
                .withRel("edit"));
        responseEntityModel.add(linkTo(methodOn(CategoryRestResource.class)
                .deleteEntity(response.getUid()))
                .withRel("remove"));
        responseEntityModel.add(linkTo(methodOn(CategoryRestResource.class)
                .getAllEntities(defaultPage, defaultLimit, "", defaultOrder))
                .withRel(IanaLinkRelations.COLLECTION));

        return responseEntityModel;
    }

    @Override
    public CollectionModel<EntityModel<CategoryResponse>> toCollectionModel(Iterable<? extends CategoryResponse> responses) {
        List<EntityModel<CategoryResponse>> responseEntityModels = new ArrayList<>();

        responses.forEach(response -> responseEntityModels.add(toModel(response)));

        return CollectionModel.of(responseEntityModels);
    }

    @Override
    public CollectionModel<EntityModel<CategoryResponse>> getCollectionModel(Iterable<? extends CategoryResponse> responses, PagingAndSorting ps) {
        CollectionModel<EntityModel<CategoryResponse>> responseCollectionModel = toCollectionModel(responses);

        responseCollectionModel.add(linkTo(methodOn(CategoryRestResource.class)
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
    public void addNextLink(CollectionModel<EntityModel<CategoryResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(CategoryRestResource.class)
                .getAllEntities(ps.getPage() + 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.NEXT));
    }

    @Override
    public void addPrevLink(CollectionModel<EntityModel<CategoryResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(CategoryRestResource.class)
                .getAllEntities(ps.getPage() - 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.PREV));
    }
}

package com.azad.basicecommerce.api.assembler;

import com.azad.basicecommerce.api.resource.StoreRestResource;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiResponseModelAssembler;
import com.azad.basicecommerce.model.store.StoreRequest;
import com.azad.basicecommerce.model.store.StoreResponse;
import com.azad.basicecommerce.repository.StoreRepository;
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
public class StoreResponseModelAssembler implements GenericApiResponseModelAssembler<StoreResponse> {

    @Autowired
    private StoreRepository repository;

    @Override
    public EntityModel<StoreResponse> toModel(StoreResponse response) {
        EntityModel<StoreResponse> responseEntityModel = EntityModel.of(response);

        responseEntityModel.add(linkTo(methodOn(StoreRestResource.class)
                .getEntity(response.getUid()))
                .withSelfRel());
        responseEntityModel.add(linkTo(methodOn(StoreRestResource.class)
                .updateEntity(response.getUid(), new StoreRequest()))
                .withRel("edit"));
        responseEntityModel.add(linkTo(methodOn(StoreRestResource.class)
                .deleteEntity(response.getUid()))
                .withRel("remove"));
        responseEntityModel.add(linkTo(methodOn(StoreRestResource.class)
                .getAllEntities(defaultPage, defaultLimit, "", defaultOrder))
                .withRel(IanaLinkRelations.COLLECTION));

        return responseEntityModel;
    }

    @Override
    public CollectionModel<EntityModel<StoreResponse>> toCollectionModel(Iterable<? extends StoreResponse> responses) {
        List<EntityModel<StoreResponse>> responseEntityModels = new ArrayList<>();

        responses.forEach(response -> responseEntityModels.add(toModel(response)));

        return CollectionModel.of(responseEntityModels);
    }

    @Override
    public CollectionModel<EntityModel<StoreResponse>> getCollectionModel(Iterable<? extends StoreResponse> responses, PagingAndSorting ps) {
        CollectionModel<EntityModel<StoreResponse>> responseCollectionModel = toCollectionModel(responses);

        responseCollectionModel.add(linkTo(methodOn(StoreRestResource.class)
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
    public void addNextLink(CollectionModel<EntityModel<StoreResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(StoreRestResource.class)
                .getAllEntities(ps.getPage() + 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.NEXT));
    }

    @Override
    public void addPrevLink(CollectionModel<EntityModel<StoreResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(StoreRestResource.class)
                .getAllEntities(ps.getPage() - 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.PREV));
    }
}

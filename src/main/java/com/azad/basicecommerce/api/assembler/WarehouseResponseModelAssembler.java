package com.azad.basicecommerce.api.assembler;

import com.azad.basicecommerce.api.resource.WarehouseRestResource;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiResponseModelAssembler;
import com.azad.basicecommerce.model.warehouse.WarehouseRequest;
import com.azad.basicecommerce.model.warehouse.WarehouseResponse;
import com.azad.basicecommerce.repository.WarehouseRepository;
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
public class WarehouseResponseModelAssembler implements GenericApiResponseModelAssembler<WarehouseResponse> {

    @Autowired
    private WarehouseRepository repository;

    @Override
    public EntityModel<WarehouseResponse> toModel(WarehouseResponse response) {
        EntityModel<WarehouseResponse> responseEntityModel = EntityModel.of(response);

        responseEntityModel.add(linkTo(methodOn(WarehouseRestResource.class)
                .getEntity(response.getUid()))
                .withSelfRel());
        responseEntityModel.add(linkTo(methodOn(WarehouseRestResource.class)
                .updateEntity(response.getUid(), new WarehouseRequest()))
                .withRel("edit"));
        responseEntityModel.add(linkTo(methodOn(WarehouseRestResource.class)
                .deleteEntity(response.getUid()))
                .withRel("remove"));
        responseEntityModel.add(linkTo(methodOn(WarehouseRestResource.class)
                .getAllEntities(defaultPage, defaultLimit, "", defaultOrder))
                .withRel(IanaLinkRelations.COLLECTION));

        return responseEntityModel;
    }

    @Override
    public CollectionModel<EntityModel<WarehouseResponse>> toCollectionModel(Iterable<? extends WarehouseResponse> responses) {
        List<EntityModel<WarehouseResponse>> responseEntityModels = new ArrayList<>();

        responses.forEach(response -> responseEntityModels.add(toModel(response)));

        return CollectionModel.of(responseEntityModels);
    }

    @Override
    public CollectionModel<EntityModel<WarehouseResponse>> getCollectionModel(Iterable<? extends WarehouseResponse> responses, PagingAndSorting ps) {
        CollectionModel<EntityModel<WarehouseResponse>> responseCollectionModel = toCollectionModel(responses);

        responseCollectionModel.add(linkTo(methodOn(WarehouseRestResource.class)
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
    public void addNextLink(CollectionModel<EntityModel<WarehouseResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(WarehouseRestResource.class)
                .getAllEntities(ps.getPage() + 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.NEXT));
    }

    @Override
    public void addPrevLink(CollectionModel<EntityModel<WarehouseResponse>> collectionModel, PagingAndSorting ps) {
        collectionModel.add(linkTo(methodOn(WarehouseRestResource.class)
                .getAllEntities(ps.getPage() - 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
                .withRel(IanaLinkRelations.PREV));
    }
}

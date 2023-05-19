package com.azad.basicecommerce.api.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//
//@Component
//public class ReviewResponseModelAssembler implements GenericApiResponseModelAssembler<ReviewResponse> {
//
//    @Autowired
//    private ReviewRepository repository;
//
//    @Override
//    public EntityModel<ReviewResponse> toModel(ReviewResponse response) {
//        EntityModel<ReviewResponse> responseEntityModel = EntityModel.of(response);
//
//        responseEntityModel.add(linkTo(methodOn(ReviewRestResource.class)
//                .getEntity(response.getProductUid()))
//                .withSelfRel());
//        responseEntityModel.add(linkTo(methodOn(ReviewRestResource.class)
//                .updateEntity(response.getProductUid(), new ReviewRequest()))
//                .withRel("edit"));
//        responseEntityModel.add(linkTo(methodOn(ReviewRestResource.class)
//                .deleteEntity(response.getProductUid()))
//                .withRel("remove"));
//        responseEntityModel.add(linkTo(methodOn(ReviewRestResource.class)
//                .getAllEntities(defaultPage, defaultLimit, "", defaultOrder))
//                .withRel(IanaLinkRelations.COLLECTION));
//
//        return responseEntityModel;
//    }
//
//    @Override
//    public CollectionModel<EntityModel<ReviewResponse>> toCollectionModel(Iterable<? extends ReviewResponse> responses) {
//        List<EntityModel<ReviewResponse>> responseEntityModels = new ArrayList<>();
//
//        responses.forEach(response -> responseEntityModels.add(toModel(response)));
//
//        return CollectionModel.of(responseEntityModels);
//    }
//
//    @Override
//    public CollectionModel<EntityModel<ReviewResponse>> getCollectionModel(Iterable<? extends ReviewResponse> responses, PagingAndSorting ps) {
//        CollectionModel<EntityModel<ReviewResponse>> responseCollectionModel = toCollectionModel(responses);
//
//        responseCollectionModel.add(linkTo(methodOn(ReviewRestResource.class)
//                .getAllEntities(ps.getPage(), ps.getLimit(), ps.getSort(), ps.getOrder()))
//                .withSelfRel());
//
//        long totalRecords = repository.count();
//
//        if (ps.getPage() > 1)
//            addPrevLink(responseCollectionModel, ps);
//        if (totalRecords > (long) ps.getPage() * ps.getLimit())
//            addNextLink(responseCollectionModel, ps);
//
//        return responseCollectionModel;
//    }
//
//    @Override
//    public void addNextLink(CollectionModel<EntityModel<ReviewResponse>> collectionModel, PagingAndSorting ps) {
//        collectionModel.add(linkTo(methodOn(ReviewRestResource.class)
//                .getAllEntities(ps.getPage() + 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
//                .withRel(IanaLinkRelations.NEXT));
//    }
//
//    @Override
//    public void addPrevLink(CollectionModel<EntityModel<ReviewResponse>> collectionModel, PagingAndSorting ps) {
//        collectionModel.add(linkTo(methodOn(ReviewRestResource.class)
//                .getAllEntities(ps.getPage() - 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
//                .withRel(IanaLinkRelations.PREV));
//    }
//}

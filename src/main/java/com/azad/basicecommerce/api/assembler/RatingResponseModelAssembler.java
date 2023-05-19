//package com.azad.basicecommerce.api.assembler;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.IanaLinkRelations;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//
//@Component
//public class RatingResponseModelAssembler implements GenericApiResponseModelAssembler<RatingResponse> {
//
////    @Autowired
////    private RatingRepository repository;
////
////    @Override
////    public EntityModel<RatingResponse> toModel(RatingResponse response) {
////        EntityModel<RatingResponse> responseEntityModel = EntityModel.of(response);
////
////        responseEntityModel.add(linkTo(methodOn(RatingRestResource.class)
////                .getEntity(response.getProductUid()))
////                .withSelfRel());
////        responseEntityModel.add(linkTo(methodOn(RatingRestResource.class)
////                .updateEntity(response.getProductUid(), new RatingRequest()))
////                .withRel("edit"));
////        responseEntityModel.add(linkTo(methodOn(RatingRestResource.class)
////                .deleteEntity(response.getProductUid()))
////                .withRel("remove"));
////        responseEntityModel.add(linkTo(methodOn(RatingRestResource.class)
////                .getAllEntities(defaultPage, defaultLimit, "", defaultOrder))
////                .withRel(IanaLinkRelations.COLLECTION));
////
////        return responseEntityModel;
////    }
////
////    @Override
////    public CollectionModel<EntityModel<RatingResponse>> toCollectionModel(Iterable<? extends RatingResponse> responses) {
////        List<EntityModel<RatingResponse>> responseEntityModels = new ArrayList<>();
////
////        responses.forEach(response -> responseEntityModels.add(toModel(response)));
////
////        return CollectionModel.of(responseEntityModels);
////    }
////
////    @Override
////    public CollectionModel<EntityModel<RatingResponse>> getCollectionModel(Iterable<? extends RatingResponse> responses, PagingAndSorting ps) {
////        CollectionModel<EntityModel<RatingResponse>> responseCollectionModel = toCollectionModel(responses);
////
////        responseCollectionModel.add(linkTo(methodOn(RatingRestResource.class)
////                .getAllEntities(ps.getPage(), ps.getLimit(), ps.getSort(), ps.getOrder()))
////                .withSelfRel());
////
////        long totalRecords = repository.count();
////
////        if (ps.getPage() > 1)
////            addPrevLink(responseCollectionModel, ps);
////        if (totalRecords > (long) ps.getPage() * ps.getLimit())
////            addNextLink(responseCollectionModel, ps);
////
////        return responseCollectionModel;
////    }
////
////    @Override
////    public void addNextLink(CollectionModel<EntityModel<RatingResponse>> collectionModel, PagingAndSorting ps) {
////        collectionModel.add(linkTo(methodOn(RatingRestResource.class)
////                .getAllEntities(ps.getPage() + 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
////                .withRel(IanaLinkRelations.NEXT));
////    }
////
////    @Override
////    public void addPrevLink(CollectionModel<EntityModel<RatingResponse>> collectionModel, PagingAndSorting ps) {
////        collectionModel.add(linkTo(methodOn(RatingRestResource.class)
////                .getAllEntities(ps.getPage() - 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
////                .withRel(IanaLinkRelations.PREV));
////    }
//}

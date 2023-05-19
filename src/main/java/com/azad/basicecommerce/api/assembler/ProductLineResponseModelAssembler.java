//package com.azad.basicecommerce.api.assembler;
//
//import com.azad.basicecommerce.api.resources.ProductLineRestResource;
//import com.azad.basicecommerce.common.GenericApiResponseModelAssembler;
//import com.azad.basicecommerce.common.PagingAndSorting;
//import com.azad.basicecommerce.productservice.models.productline.ProductLineRequest;
//import com.azad.basicecommerce.productservice.models.productline.ProductLineResponse;
//import com.azad.basicecommerce.productservice.repositories.ProductLineRepository;
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
//public class ProductLineResponseModelAssembler implements GenericApiResponseModelAssembler<ProductLineResponse> {
//
//    @Autowired
//    private ProductLineRepository repository;
//
//    @Override
//    public EntityModel<ProductLineResponse> toModel(ProductLineResponse response) {
//        EntityModel<ProductLineResponse> responseEntityModel = EntityModel.of(response);
//
//        responseEntityModel.add(linkTo(methodOn(ProductLineRestResource.class)
//                .getEntity(response.getProductLineUid()))
//                .withSelfRel());
//        responseEntityModel.add(linkTo(methodOn(ProductLineRestResource.class)
//                .updateEntity(response.getProductLineUid(), new ProductLineRequest()))
//                .withRel("edit"));
//        responseEntityModel.add(linkTo(methodOn(ProductLineRestResource.class)
//                .deleteEntity(response.getProductLineUid()))
//                .withRel("remove"));
//        responseEntityModel.add(linkTo(methodOn(ProductLineRestResource.class)
//                .getAllEntities(defaultPage, defaultLimit, "", defaultOrder))
//                .withRel(IanaLinkRelations.COLLECTION));
//
//        return responseEntityModel;
//    }
//
//    @Override
//    public CollectionModel<EntityModel<ProductLineResponse>> toCollectionModel(Iterable<? extends ProductLineResponse> responses) {
//        List<EntityModel<ProductLineResponse>> responseEntityModels = new ArrayList<>();
//
//        responses.forEach(response -> responseEntityModels.add(toModel(response)));
//
//        return CollectionModel.of(responseEntityModels);
//    }
//
//    @Override
//    public CollectionModel<EntityModel<ProductLineResponse>> getCollectionModel(Iterable<? extends ProductLineResponse> responses, PagingAndSorting ps) {
//        CollectionModel<EntityModel<ProductLineResponse>> responseCollectionModel = toCollectionModel(responses);
//
//        responseCollectionModel.add(linkTo(methodOn(ProductLineRestResource.class)
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
//    public void addNextLink(CollectionModel<EntityModel<ProductLineResponse>> collectionModel, PagingAndSorting ps) {
//        collectionModel.add(linkTo(methodOn(ProductLineRestResource.class)
//                .getAllEntities(ps.getPage() + 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
//                .withRel(IanaLinkRelations.NEXT));
//    }
//
//    @Override
//    public void addPrevLink(CollectionModel<EntityModel<ProductLineResponse>> collectionModel, PagingAndSorting ps) {
//        collectionModel.add(linkTo(methodOn(ProductLineRestResource.class)
//                .getAllEntities(ps.getPage() - 1, ps.getLimit(), ps.getSort(), ps.getOrder()))
//                .withRel(IanaLinkRelations.PREV));
//    }
//}

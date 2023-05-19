//package com.azad.basicecommerce.api.resource;
//
//import com.azad.basicecommerce.api.assembler.StoreResponseModelAssembler;
//import com.azad.basicecommerce.common.ApiUtils;
//import com.azad.basicecommerce.common.PagingAndSorting;
//import com.azad.basicecommerce.common.generics.GenericApiRestController;
//import com.azad.basicecommerce.model.store.StoreDto;
//import com.azad.basicecommerce.model.store.StoreRequest;
//import com.azad.basicecommerce.model.store.StoreResponse;
//import com.azad.basicecommerce.service.store.StoreService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping(path = "/api/v1/inventoryservice/stores")
//public class StoreRestResource implements GenericApiRestController<StoreRequest, StoreResponse> {
//
//    @Autowired
//    private ApiUtils apiUtils;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    private final StoreService service;
//    private final StoreResponseModelAssembler assembler;
//
//    @Autowired
//    public StoreRestResource(StoreService service, StoreResponseModelAssembler assembler) {
//        this.service = service;
//        this.assembler = assembler;
//    }
//
//    @PostMapping
//    @Override
//    public ResponseEntity<EntityModel<StoreResponse>> createEntity(@Valid @RequestBody StoreRequest request) {
//
//        apiUtils.printRequestInfo("/api/v1/inventory/stores", "POST", "USER, SELLER");
//
//        StoreDto dto = modelMapper.map(request, StoreDto.class);
//
//        StoreDto savedDto = service.create(dto);
//
//        return new ResponseEntity<>(assembler.toModel(modelMapper.map(savedDto, StoreResponse.class)),
//                HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    @Override
//    public ResponseEntity<CollectionModel<EntityModel<StoreResponse>>> getAllEntities(
//            @Valid @RequestParam(name = "page", defaultValue = "1") int page,
//            @Valid @RequestParam(name = "limit", defaultValue = "25") int limit,
//            @Valid @RequestParam(name = "sort", defaultValue = "") String sort,
//            @Valid @RequestParam(name = "order", defaultValue = "asc") String order) {
//
//        apiUtils.printRequestInfo("/api/v1/inventoryservice/stores", "GET", "USER, SELLER");
//
//        if (page < 0) page = 0;
//
//        List<StoreDto> dtosFromService = service.getAll(
//                new PagingAndSorting(page > 0 ? page - 1 : page, limit, sort, order));
//        if (dtosFromService == null || dtosFromService.size() == 0)
//            return ResponseEntity.noContent().build();
//
//        List<StoreResponse> responses = dtosFromService.stream()
//                .map(dto -> modelMapper.map(dto, StoreResponse.class))
//                .collect(Collectors.toList());
//
//        CollectionModel<EntityModel<StoreResponse>> responseCollectionModel =
//                assembler.getCollectionModel(responses, new PagingAndSorting(page, limit, sort, order));
//
//        return new ResponseEntity<>(responseCollectionModel, HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<EntityModel<StoreResponse>> getEntity(Long id) {
//        return null;
//    }
//
//    @GetMapping(path = "/{storeUid}")
//    @Override
//    public ResponseEntity<EntityModel<StoreResponse>> getEntity(@Valid @PathVariable("storeUid") String storeUid) {
//
//        apiUtils.printRequestInfo("/api/v1/inventoryservice/stores/" + storeUid, "GET", "USER, SELLER");
//
//        StoreDto dtoFromService;
//        try {
//            dtoFromService = service.getByUid(storeUid);
//        } catch (RuntimeException ex) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, StoreResponse.class)),
//                HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<EntityModel<StoreResponse>> updateEntity(Long id, StoreRequest updatedRequest) {
//        return null;
//    }
//
//    @PutMapping(path = "/{storeUid}")
//    @Override
//    public ResponseEntity<EntityModel<StoreResponse>> updateEntity(
//            @Valid @PathVariable("storeUid") String storeUid, @RequestBody StoreRequest updatedRequest) {
//
//        apiUtils.printRequestInfo("/api/v1/inventoryservice/stores/" + storeUid, "PUT", "USER, SELLER");
//
//        StoreDto dtoFromService;
//        try {
//            dtoFromService = service.updateByUid(storeUid, modelMapper.map(updatedRequest, StoreDto.class));
//        } catch (RuntimeException ex) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, StoreResponse.class)),
//                HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<?> deleteEntity(Long id) {
//        return null;
//    }
//
//    @DeleteMapping(path = "/{storeUid}")
//    @Override
//    public ResponseEntity<?> deleteEntity(@Valid @PathVariable("storeUid") String storeUid) {
//
//        apiUtils.printRequestInfo("/api/v1/inventoryservice/stores/" + storeUid, "DELETE", "USER, SELLER");
//
//        service.deleteByUid(storeUid);
//
//        return ResponseEntity.ok().build();
//    }
//}

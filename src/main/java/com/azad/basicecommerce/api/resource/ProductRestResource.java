package com.azad.basicecommerce.api.resource;

import com.azad.basicecommerce.api.assembler.ProductResponseModelAssembler;
import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiRestController;
import com.azad.basicecommerce.model.product.ProductDto;
import com.azad.basicecommerce.model.product.ProductRequest;
import com.azad.basicecommerce.model.product.ProductResponse;
import com.azad.basicecommerce.service.productservice.product.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/productservice/products")
public class ProductRestResource implements GenericApiRestController<ProductRequest, ProductResponse> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    private final ProductService service;
    private final ProductResponseModelAssembler assembler;

    @Autowired
    public ProductRestResource(ProductService service, ProductResponseModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    @Override
    public ResponseEntity<EntityModel<ProductResponse>> createEntity(@Valid @RequestBody ProductRequest request) {

        apiUtils.printRequestInfo("/api/v1/productservice/products", "POST", "SELLER");

        ProductDto dto = modelMapper.map(request, ProductDto.class);

        ProductDto savedDto = service.create(dto);

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(savedDto, ProductResponse.class)),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<ProductResponse>>> getAllEntities(int page, int limit, String sort, String order) {
        return null;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductResponse>>> getAllEntities(
            @Valid @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @RequestParam(name = "limit", defaultValue = "25") int limit,
            @Valid @RequestParam(name = "sort", defaultValue = "") String sort,
            @Valid @RequestParam(name = "order", defaultValue = "asc") String order,
            @Valid @RequestParam(name = "uid", defaultValue = "") String uid) {

        apiUtils.printRequestInfo("/api/v1/productservice/products", "GET", "USER, SELLER, ADMIN");

        if (page < 0) page = 0;

        List<ProductDto> dtosFromService;
        if (uid == null || uid.isEmpty())
            dtosFromService = service.getAll(new PagingAndSorting(page > 0 ? page - 1 : page, limit, sort, order));
        else
            dtosFromService = service.getAllByUid(uid, new PagingAndSorting(page > 0 ? page - 1 : page, limit, sort, order));

        return prepareGetAllApiResponse(dtosFromService, new PagingAndSorting(page, limit, sort, order));
    }

    @Override
    public ResponseEntity<EntityModel<ProductResponse>> getEntity(Long id) {
        return null;
    }

    @GetMapping(path = "/{productUid}")
    @Override
    public ResponseEntity<EntityModel<ProductResponse>> getEntity(@Valid @PathVariable("productUid") String uid) {

        apiUtils.printRequestInfo("/api/v1/productservice/products/" + uid, "GET", "USER, SELLER, ADMIN");

        ProductDto dtoFromService = service.getByUid(uid);
        if (dtoFromService == null)
            return ResponseEntity.notFound().build();

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, ProductResponse.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EntityModel<ProductResponse>> updateEntity(Long id, ProductRequest updatedRequest) {
        return null;
    }

    @PutMapping(path = "/{productUid}")
    @Override
    public ResponseEntity<EntityModel<ProductResponse>> updateEntity(
            @Valid @PathVariable("productUid") String uid, @RequestBody ProductRequest updatedRequest) {

        apiUtils.printRequestInfo("/api/v1/productservice/products/" + uid, "PUT", "SELLER");

        ProductDto dtoFromService = service.updateByUid(uid, modelMapper.map(updatedRequest, ProductDto.class));
        if (dtoFromService == null)
            return ResponseEntity.badRequest().build();

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, ProductResponse.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteEntity(Long id) {
        return null;
    }

    @DeleteMapping(path = "/{productUid}")
    @Override
    public ResponseEntity<?> deleteEntity(@Valid @PathVariable("productUid") String uid) {

        apiUtils.printRequestInfo("/api/v1/productservice/products/" + uid, "DELETE", "SELLER, ADMIN");

        service.deleteByUid(uid);

        return ResponseEntity.ok("Product Deleted");
    }

    private ResponseEntity<CollectionModel<EntityModel<ProductResponse>>> prepareGetAllApiResponse(List<ProductDto> dtos, PagingAndSorting ps) {

        if (dtos == null || dtos.size() == 0)
            return ResponseEntity.noContent().build();

        return new ResponseEntity<>(assembler.getCollectionModel(dtos.stream()
                        .map(dto -> modelMapper.map(dto, ProductResponse.class))
                        .collect(Collectors.toList()), ps),
                HttpStatus.OK);
    }
}

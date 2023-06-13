package com.azad.basicecommerce.api.resource;

import com.azad.basicecommerce.api.assembler.RatingResponseModelAssembler;
import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiRestController;
import com.azad.basicecommerce.model.rating.RatingDto;
import com.azad.basicecommerce.model.rating.RatingRequest;
import com.azad.basicecommerce.model.rating.RatingResponse;
import com.azad.basicecommerce.service.productservice.rating.RatingService;
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
@RequestMapping(path = "/api/v1/productservice/ratings")
public class RatingRestResource implements GenericApiRestController<RatingRequest, RatingResponse> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    private final RatingService service;
    private final RatingResponseModelAssembler assembler;

    @Autowired
    public RatingRestResource(RatingService service, RatingResponseModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    @Override
    public ResponseEntity<EntityModel<RatingResponse>> createEntity(@Valid @RequestBody RatingRequest request) {

        apiUtils.printRequestInfo("/api/v1/productservice/ratings", "POST", "USER");

        RatingDto dto = modelMapper.map(request, RatingDto.class);

        RatingDto savedDto = service.create(dto);

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(savedDto, RatingResponse.class)),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<RatingResponse>>> getAllEntities(int page, int limit, String sort, String order) {
        return null;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<RatingResponse>>> getAllEntitiesByProduct(
            @Valid @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @RequestParam(name = "limit", defaultValue = "25") int limit,
            @Valid @RequestParam(name = "sort", defaultValue = "") String sort,
            @Valid @RequestParam(name = "order", defaultValue = "asc") String order,
            @Valid @RequestParam(name = "productUid") String productUid) {

        apiUtils.printRequestInfo("/api/v1/productservice/ratings", "GET", "USER, SELLER, ADMIN");

        if (page < 0) page = 0;

        List<RatingDto> dtosFromService;
        if (productUid == null || productUid.isEmpty() || productUid.equalsIgnoreCase("0")) {
            dtosFromService = service.getAll(new PagingAndSorting(page > 1 ? page - 1 : page, limit, sort, order));
        }
        else {
            dtosFromService = service.getAllByProduct(productUid, new PagingAndSorting(page > 1 ? page - 1 : page, limit, sort, order));
        }
        if (dtosFromService == null || dtosFromService.size() == 0)
            return ResponseEntity.noContent().build();

        return new ResponseEntity<>(assembler.getCollectionModel(dtosFromService.stream()
                    .map(dto -> modelMapper.map(dto, RatingResponse.class))
                    .collect(Collectors.toList()),
                new PagingAndSorting(page, limit, sort, order)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EntityModel<RatingResponse>> getEntity(Long id) {
        return null;
    }

    @GetMapping(path = "/{ratingUid}")
    @Override
    public ResponseEntity<EntityModel<RatingResponse>> getEntity(@Valid @PathVariable("ratingUid") String uid) {

        apiUtils.printRequestInfo("/api/v1/productservice/ratings/" + uid, "GET", "USER, SELLER, ADMIN");

        RatingDto dtoFromService = service.getByUid(uid);
        if (dtoFromService == null)
            return ResponseEntity.notFound().build();

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, RatingResponse.class)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EntityModel<RatingResponse>> updateEntity(Long id, RatingRequest updatedRequest) {
        return null;
    }

    @PutMapping(path = "/{ratingUid}")
    @Override
    public ResponseEntity<EntityModel<RatingResponse>> updateEntity(
            @Valid @PathVariable("ratingUid")String uid, @RequestBody RatingRequest updatedRequest) {

        apiUtils.printRequestInfo("/api/v1/productservice/ratings/" + uid, "PUT", "USER");

        RatingDto dtoFromService = service.updateByUid(uid, modelMapper.map(updatedRequest, RatingDto.class));
        if (dtoFromService == null)
            return ResponseEntity.badRequest().build();

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, RatingResponse.class)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteEntity(Long id) {
        return null;
    }

    @DeleteMapping(path = "/{ratingUid}")
    @Override
    public ResponseEntity<?> deleteEntity(@Valid @PathVariable("ratingUid") String uid) {

        apiUtils.printRequestInfo("/api/v1/productservice/ratings/" + uid, "DELETE", "USER");

        service.deleteByUid(uid);

        return ResponseEntity.ok("Rating Deleted");
    }
}

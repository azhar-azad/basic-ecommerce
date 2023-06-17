package com.azad.basicecommerce.api.resource;

import com.azad.basicecommerce.api.assembler.ReviewResponseModelAssembler;
import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.generics.GenericApiRestController;
import com.azad.basicecommerce.model.review.ReviewDto;
import com.azad.basicecommerce.model.review.ReviewRequest;
import com.azad.basicecommerce.model.review.ReviewResponse;
import com.azad.basicecommerce.service.productservice.review.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/productservice/reviews")
public class ReviewRestResource implements GenericApiRestController<ReviewRequest, ReviewResponse> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    private final ReviewService service;
    private final ReviewResponseModelAssembler assembler;

    @Autowired
    public ReviewRestResource(ReviewService service, ReviewResponseModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    @Override
    public ResponseEntity<EntityModel<ReviewResponse>> createEntity(@Valid @RequestBody ReviewRequest request) {

        apiUtils.printRequestInfo("/api/v1/productservice/reviews", "POST", "USER");

        ReviewDto dto = modelMapper.map(request, ReviewDto.class);

        ReviewDto savedDto = service.create(dto);

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(savedDto, ReviewResponse.class)),
                HttpStatus.CREATED);
    }

    @GetMapping
    @Override
    public ResponseEntity<CollectionModel<EntityModel<ReviewResponse>>> getAllEntities(
            @Valid @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @RequestParam(name = "limit", defaultValue = "1") int limit,
            @Valid @RequestParam(name = "sort", defaultValue = "1") String sort,
            @Valid @RequestParam(name = "order", defaultValue = "1") String order) {

        apiUtils.printRequestInfo("/api/v1/productservice/reviews", "GET", "USER, SELLER, ADMIN");

        if (page < 0) page = 0;

        return null;
    }

    @Override
    public ResponseEntity<EntityModel<ReviewResponse>> getEntity(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<EntityModel<ReviewResponse>> getEntity(String uid) {
        return null;
    }

    @Override
    public ResponseEntity<EntityModel<ReviewResponse>> updateEntity(Long id, ReviewRequest updatedRequest) {
        return null;
    }

    @Override
    public ResponseEntity<EntityModel<ReviewResponse>> updateEntity(String uid, ReviewRequest updatedRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteEntity(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteEntity(String uid) {
        return null;
    }
}

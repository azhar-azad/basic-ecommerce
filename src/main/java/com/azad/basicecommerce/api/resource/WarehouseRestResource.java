package com.azad.basicecommerce.api.resource;

import com.azad.basicecommerce.api.assembler.WarehouseResponseModelAssembler;
import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.common.generics.GenericApiRestController;
import com.azad.basicecommerce.model.warehouse.WarehouseDto;
import com.azad.basicecommerce.model.warehouse.WarehouseRequest;
import com.azad.basicecommerce.model.warehouse.WarehouseResponse;
import com.azad.basicecommerce.service.inventoryservice.warehouse.WarehouseService;
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
@RequestMapping(path = "/api/v1/inventoryservice/warehouses")
public class WarehouseRestResource implements GenericApiRestController<WarehouseRequest, WarehouseResponse> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    private final WarehouseService service;
    private final WarehouseResponseModelAssembler assembler;

    @Autowired
    public WarehouseRestResource(WarehouseService service, WarehouseResponseModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    @Override
    public ResponseEntity<EntityModel<WarehouseResponse>> createEntity(@Valid @RequestBody WarehouseRequest request) {

        apiUtils.printRequestInfo("/api/v1/inventoryservice/warehouses", "POST", "SELLER, ADMIN");

        WarehouseDto dto = modelMapper.map(request, WarehouseDto.class);

        WarehouseDto savedDto = service.create(dto);

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(savedDto, WarehouseResponse.class)),
                HttpStatus.CREATED);
    }

    @GetMapping
    @Override
    public ResponseEntity<CollectionModel<EntityModel<WarehouseResponse>>> getAllEntities(
            @Valid @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @RequestParam(name = "limit", defaultValue = "25") int limit,
            @Valid @RequestParam(name = "sort", defaultValue = "") String sort,
            @Valid @RequestParam(name = "order", defaultValue = "asc") String order) {

        apiUtils.printRequestInfo("/api/v1/inventoryservice/warehouses", "GET", "SELLER, ADMIN");

        if (page < 0) page = 0;

        List<WarehouseDto> dtosFromService = service.getAll(
                new PagingAndSorting(page > 0 ? page - 1 : page, limit, sort, order));
        if (dtosFromService == null || dtosFromService.size() == 0)
            return ResponseEntity.noContent().build();

        return new ResponseEntity<>(assembler.getCollectionModel(dtosFromService.stream()
                        .map(dto -> modelMapper.map(dto, WarehouseResponse.class))
                        .collect(Collectors.toList()),
                new PagingAndSorting(page, limit, sort, order)),
                HttpStatus.OK);
    }

    @GetMapping(path = "/byStore/{storeUid}")
    public ResponseEntity<CollectionModel<EntityModel<WarehouseResponse>>> getAllEntities(
            @Valid @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @RequestParam(name = "limit", defaultValue = "25") int limit,
            @Valid @RequestParam(name = "sort", defaultValue = "") String sort,
            @Valid @RequestParam(name = "order", defaultValue = "asc") String order,
            @Valid @PathVariable("storeUid") String storeUid) {

        apiUtils.printRequestInfo("/api/v1/inventoryservice/warehouses", "GET", "SELLER, ADMIN");

        if (page < 0) page = 0;

        List<WarehouseDto> dtosFromService = service.getAllByStore(storeUid,
                new PagingAndSorting(page > 0 ? page - 1 : page, limit, sort, order));
        if (dtosFromService == null || dtosFromService.size() == 0)
            return ResponseEntity.noContent().build();

        return new ResponseEntity<>(assembler.getCollectionModel(dtosFromService.stream()
                        .map(dto -> modelMapper.map(dto, WarehouseResponse.class))
                        .collect(Collectors.toList()),
                new PagingAndSorting(page, limit, sort, order)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EntityModel<WarehouseResponse>> getEntity(Long id) {
        return null;
    }

    @GetMapping(path = "/{warehouseUid}")
    @Override
    public ResponseEntity<EntityModel<WarehouseResponse>> getEntity(@Valid @PathVariable("warehouseUid") String warehouseUid) {

        apiUtils.printRequestInfo("/api/v1/inventoryservice/warehouses/" + warehouseUid, "GET", "SELLER, ADMIN");

        WarehouseDto dtoFromService = service.getByUid(warehouseUid);
        if (dtoFromService == null)
            return ResponseEntity.notFound().build();

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, WarehouseResponse.class)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EntityModel<WarehouseResponse>> updateEntity(Long id, WarehouseRequest updatedRequest) {
        return null;
    }

    @PutMapping(path = "/{warehouseUid}")
    @Override
    public ResponseEntity<EntityModel<WarehouseResponse>> updateEntity(
            @Valid @PathVariable("warehouseUid") String warehouseUid, @RequestBody WarehouseRequest updatedRequest) {

        apiUtils.printRequestInfo("/api/v1/inventoryservice/warehouses/" + warehouseUid, "PUT", "SELLER, ADMIN");

        WarehouseDto dtoFromService = service.updateByUid(warehouseUid, modelMapper.map(updatedRequest, WarehouseDto.class));
        if (dtoFromService == null)
            return ResponseEntity.badRequest().build();

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(dtoFromService, WarehouseResponse.class)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteEntity(Long id) {
        return null;
    }

    @DeleteMapping(path = "/{warehouseUid}")
    @Override
    public ResponseEntity<?> deleteEntity(@Valid @PathVariable("warehouseUid") String warehouseUid) {
        apiUtils.printRequestInfo("/api/v1/inventoryservice/warehouses/" + warehouseUid, "DELETE", "SELLER, ADMIN");

        service.deleteByUid(warehouseUid);

        return ResponseEntity.ok("Warehouse Deleted");
    }
}

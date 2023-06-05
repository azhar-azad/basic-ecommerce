package com.azad.basicecommerce.service.productservice.category;

import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.model.category.CategoryDto;
import com.azad.basicecommerce.model.category.CategoryEntity;
import com.azad.basicecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDto create(CategoryDto dto) {

        apiUtils.logInfo("*** CATEGORY :: CREATE ***");

        CategoryEntity entity = modelMapper.map(dto, CategoryEntity.class);
        entity.setUid(apiUtils.generateCategoryUid(entity.getCategoryName()));

        CategoryEntity savedEntity = repository.save(entity);

        return modelMapper.map(savedEntity, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAll(PagingAndSorting ps) {

        apiUtils.logInfo("*** CATEGORY :: GET ALL ***");

        List<CategoryEntity> entitiesFromDb = repository.findAll(apiUtils.getPageable(ps)).getContent();
        if (entitiesFromDb.size() == 0)
            return null;

        return entitiesFromDb.stream()
                .map(entity -> modelMapper.map(entity, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        return null;
    }

    @Override
    public CategoryDto getByUid(String uid) {
        return null;
    }

    @Override
    public CategoryDto updateById(Long id, CategoryDto updatedDto) {
        return null;
    }

    @Override
    public CategoryDto updateByUid(String uid, CategoryDto updatedDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByUid(String uid) {

    }

    @Override
    public Long getEntityCount() {
        return null;
    }
}

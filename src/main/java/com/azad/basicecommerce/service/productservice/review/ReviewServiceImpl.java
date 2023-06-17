package com.azad.basicecommerce.service.productservice.review;

import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.common.PagingAndSorting;
import com.azad.basicecommerce.model.review.ReviewDto;
import com.azad.basicecommerce.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    private final ReviewRepository repository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReviewDto create(ReviewDto dto) {
        return null;
    }

    @Override
    public List<ReviewDto> getAll(PagingAndSorting ps) {
        return null;
    }

    @Override
    public ReviewDto getById(Long id) {
        return null;
    }

    @Override
    public ReviewDto getByUid(String uid) {
        return null;
    }

    @Override
    public ReviewDto updateById(Long id, ReviewDto updatedDto) {
        return null;
    }

    @Override
    public ReviewDto updateByUid(String uid, ReviewDto updatedDto) {
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

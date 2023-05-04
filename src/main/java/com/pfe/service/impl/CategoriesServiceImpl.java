package com.pfe.service.impl;

import com.pfe.entity.Category;
import com.pfe.repository.CategoriesRepository;
import com.pfe.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    @Override
    public List<Category> getCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Optional<Category> getCategory(Long category) {
        return categoriesRepository.findById(category);
    }

}

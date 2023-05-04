package com.pfe.service;

import com.pfe.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoriesService {
    List<Category> getCategories();
    Optional<Category> getCategory(Long id);
}

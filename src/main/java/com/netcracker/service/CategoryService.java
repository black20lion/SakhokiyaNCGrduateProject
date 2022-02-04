package com.netcracker.service;

import com.netcracker.domain.Category;
import com.netcracker.domain.enumeration.Gender;
import com.netcracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository repository;

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public List<Category> getCategoriesByGender(Gender gender) {
        return repository.findByGender(gender);
    }
}

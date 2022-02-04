package com.netcracker.controller;

import com.netcracker.domain.Category;
import com.netcracker.domain.enumeration.Gender;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{gender}")
    public ResponseEntity<List<Category>> getCategoryById(@PathVariable(value = "gender") Gender gender)
            throws ResourceNotFoundException {
        List<Category> categories = categoryService.getCategoriesByGender(gender);
        return ResponseEntity.ok().body(categories);
    }
}

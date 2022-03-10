package com.netcracker.controller;

import com.netcracker.domain.Category;
import com.netcracker.domain.enumeration.Gender;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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

package com.netcracker.controller;


import com.netcracker.domain.Product;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    List<Product> getAllProduct() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<List<Product>> getProductById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Product> products = productService.getProductById(id);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/products/category/{id}")
    public ResponseEntity<List<Product>> getProductByCategoryId(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Product> products = productService.getProductByCategory(id);
        return ResponseEntity.ok().body(products);
    }

}

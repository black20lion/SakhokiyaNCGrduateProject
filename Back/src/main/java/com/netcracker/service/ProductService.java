package com.netcracker.service;

import com.netcracker.domain.Product;
import com.netcracker.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public List<Product> getProductByCategory(Long id) {
        return repository.findProductByCategoryId(id);
    }

}

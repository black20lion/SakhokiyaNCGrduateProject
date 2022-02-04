package com.netcracker.repository;

import com.netcracker.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAll();
    List<Product> findById(Long id);
    List<Product> findProductByCategoryId(Long id);
}

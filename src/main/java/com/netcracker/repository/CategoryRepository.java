package com.netcracker.repository;

import com.netcracker.domain.Category;
import com.netcracker.domain.enumeration.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByName(String name);
    List<Category> findAll();
    List<Category> findByGender(Gender gender);
}

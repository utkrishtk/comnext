package org.utkrisht.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.utkrisht.product.model.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByTitle(String title);



}
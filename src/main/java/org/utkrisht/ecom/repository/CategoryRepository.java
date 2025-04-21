package org.utkrisht.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.utkrisht.ecom.model.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByTitle(String title);



}
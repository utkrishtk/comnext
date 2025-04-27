package org.utkrisht.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.utkrisht.product.model.Category;
import org.utkrisht.product.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findAllByCategory(Category category, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCaseOrDescriptionIgnoreCase(String productName,String description,Pageable pageable);
    Optional<Product> findByProductName(String name);
}

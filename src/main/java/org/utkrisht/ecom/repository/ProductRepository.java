package org.utkrisht.ecom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.utkrisht.ecom.model.Category;
import org.utkrisht.ecom.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findAllByCategory(Category category, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCaseOrDescriptionIgnoreCase(String productName,String description,Pageable pageable);
}

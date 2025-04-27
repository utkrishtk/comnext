package org.utkrisht.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.utkrisht.product.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

}

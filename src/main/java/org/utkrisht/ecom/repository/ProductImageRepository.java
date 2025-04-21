package org.utkrisht.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.utkrisht.ecom.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

}

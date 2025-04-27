package org.utkrisht.product.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utkrisht.product.config.AppConstants;
import org.utkrisht.product.dto.ProductDto;
import org.utkrisht.product.dto.ProductResponse;
import org.utkrisht.product.dto.ProductsResponseDto;
import org.utkrisht.product.service.ProductService;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/api/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductDto dto, @PathVariable Long categoryId){

        return new ResponseEntity<>(productService.createProduct(dto,categoryId), HttpStatus.CREATED);

    }

    @GetMapping("/api/public/products")
    public ResponseEntity<ProductsResponseDto> getAllProducts(@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                              @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                              @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
                                                              @RequestParam(name="sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir){
        return new ResponseEntity<>(productService.getAllProducts(pageSize,pageNumber,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/api/public/categories/{categoryId}/product")
    public ResponseEntity<ProductsResponseDto> getProductByCategory(@PathVariable(name="categoryId")Long categoryId,
                                                                 @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                 @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                 @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
                                                                 @RequestParam(name="sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir){
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId,pageSize,pageNumber,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/api/public/products/keyword/{keyword}")
    public ResponseEntity<ProductsResponseDto> getProductListByKeyword(@PathVariable(name="keyword") String keyword,
                                                                    @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                    @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                    @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
                                                                    @RequestParam(name="sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir){
        return new ResponseEntity<>(productService.getProductsByKeyword(keyword,pageSize,pageNumber,sortBy,sortDir),HttpStatus.FOUND);
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "productId") Long productId,@Valid @RequestBody ProductDto dto){
        return new ResponseEntity<>(productService.updateProduct(productId,dto),HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Deleted successfully",HttpStatus.OK);
    }
}

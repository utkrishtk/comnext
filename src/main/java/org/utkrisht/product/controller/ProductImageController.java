package org.utkrisht.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utkrisht.product.service.ProductImageService;

@RestController
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping(path = "/api/products/{productId}/image",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadImage(@PathVariable(name = "productId") Long productId, @RequestParam(name = "file")MultipartFile file){
        return new ResponseEntity<>(productImageService.uploadImage(productId,file), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/product/image/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable(name = "imageId") Long imageId){
        productImageService.deleteImage(imageId);
        return new ResponseEntity<>("Deleted successfully",HttpStatus.OK);
    }
}

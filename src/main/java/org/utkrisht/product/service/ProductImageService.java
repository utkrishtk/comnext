package org.utkrisht.product.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.utkrisht.product.exceptions.APIException;
import org.utkrisht.product.exceptions.ResourceNotFoundException;
import org.utkrisht.product.model.Product;
import org.utkrisht.product.model.ProductImage;
import org.utkrisht.product.repository.ProductImageRepository;
import org.utkrisht.product.repository.ProductRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductImageService {

    private final AmazonS3 amazonS3;

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public ProductImageService(AmazonS3 amazonS3, ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.amazonS3 = amazonS3;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public String uploadImage(Long productId, MultipartFile file){

        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("productId","Not Found",productId));
        String fileName = UUID.randomUUID() + "_" +Paths.get(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString();
        try{
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType(file.getContentType());
//            System.out.println(file.getContentType());
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
        }catch (IOException e){
            throw new APIException("File failed to upload, unable to get Input Stream.");
        }
        String imageUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
        ProductImage productImage = new ProductImage();
        productImage.setImageUrl(imageUrl);
        productImage.setProduct(product);
        productImageRepository.save(productImage);
        return imageUrl;
    }
    @Transactional
    public void deleteImage(Long productImageId) {
        ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(()->new ResourceNotFoundException("productId","Not Found",productImageId));
        String imageUrl = productImage.getImageUrl();
//        System.out.println(imageUrl.substring(imageUrl.lastIndexOf(".com/") + 5));
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName,imageUrl.substring(imageUrl.lastIndexOf(".com/") + 5));
        amazonS3.deleteObject(deleteObjectRequest);
        productImageRepository.delete(productImage);
    }
}

package org.utkrisht.ecom.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.utkrisht.ecom.exceptions.APIException;
import org.utkrisht.ecom.exceptions.ResourceNotFoundException;
import org.utkrisht.ecom.model.Product;
import org.utkrisht.ecom.model.ProductImage;
import org.utkrisht.ecom.repository.ProductImageRepository;
import org.utkrisht.ecom.repository.ProductRepository;

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
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
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

    public void deleteImage(Long productImageId) {
        ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(()->new ResourceNotFoundException("productId","Not Found",productImageId));
        String imageUrl = productImage.getImageUrl();
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName,imageUrl.substring(imageUrl.lastIndexOf(".com/") + 5) );
        amazonS3.deleteObject(deleteObjectRequest);
    }
}

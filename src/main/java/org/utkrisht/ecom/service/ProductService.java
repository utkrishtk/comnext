package org.utkrisht.ecom.service;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.utkrisht.ecom.dto.ProductDto;
import org.utkrisht.ecom.dto.ProductResponse;
import org.utkrisht.ecom.exceptions.ResourceNotFoundException;
import org.utkrisht.ecom.model.Category;
import org.utkrisht.ecom.model.Product;
import org.utkrisht.ecom.repository.CategoryRepository;
import org.utkrisht.ecom.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public ProductResponse createProduct(ProductDto dto, Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","Not Found",categoryId
        ));
      dto.setCategory(category);
      Product product = productRepository.save(mapper.map(dto, Product.class));
      return new ProductResponse(mapper.map(product, ProductDto.class),"success");
    };

    public List<ProductDto> getAllProducts(Integer pageSize,Integer pageNumber,String sortBy, String sortDir){

        Sort sortByAndOrder  = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber-1,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();
        List<ProductDto> productsDto = products.stream().map(product -> mapper.map(product, ProductDto.class)).toList();

        return productsDto;
    }

    public List<ProductDto> getProductsByCategory(Long categoryId,Integer pageSize,Integer pageNumber,String sortBy,String sortDir){
        Sort sortByAndDir = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber - 1, pageSize,sortByAndDir);

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("categoryId",
                "Not Found",categoryId));
        Page<Product> productPage = productRepository.findAllByCategory(category,pageDetails);
        List<Product> products = productPage.getContent();
        List<ProductDto> productDtos = products.stream().map(product -> mapper.map(product,ProductDto.class)).toList();
        return productDtos;
    }

    public List<ProductDto> getProductsByKeyword(String keyword,Integer pageSize, Integer pageNumber,String sortBy,String sortDir){
        Sort sortByAndDir = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber - 1, pageSize, sortByAndDir);

        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCaseOrDescriptionIgnoreCase(keyword,keyword,pageDetails);
        List<Product> products = productPage.getContent();
        List<ProductDto> productDtos = products.stream().map( product -> mapper.map(product, ProductDto.class)).toList();
        return productDtos;
    }

    public ProductDto updateProduct(Long id,ProductDto dto){
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("categoryId",
                "Not Found",id));
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(dto,product);
        productRepository.save(product);
        return mapper.map(product, ProductDto.class);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("categoryId",
                "Not Found",id));
        productRepository.delete(product);
    }
}

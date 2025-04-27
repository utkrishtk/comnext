package org.utkrisht.product.service;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.utkrisht.product.dto.ProductDto;
import org.utkrisht.product.dto.ProductResponse;
import org.utkrisht.product.dto.ProductsResponseDto;
import org.utkrisht.product.exceptions.APIException;
import org.utkrisht.product.exceptions.ResourceNotFoundException;
import org.utkrisht.product.model.Category;
import org.utkrisht.product.model.Product;
import org.utkrisht.product.repository.CategoryRepository;
import org.utkrisht.product.repository.ProductRepository;

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
        if(productRepository.findByProductName(dto.getProductName()).isPresent()) throw new APIException("Product with this name already exist");
      dto.setCategory(category);
      Product product = productRepository.save(mapper.map(dto, Product.class));
      return new ProductResponse(mapper.map(product, ProductDto.class),"success");
    };

    public ProductsResponseDto getAllProducts(Integer pageSize,Integer pageNumber,String sortBy, String sortDir){

        Sort sortByAndOrder  = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber-1,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        if(productPage.getTotalElements() == 0) throw new  APIException("No product yet");
        List<Product> products = productPage.getContent();
        ProductsResponseDto productsResponseDto = new ProductsResponseDto();
        productsResponseDto.setProducts(products.stream().map(product -> mapper.map(product, ProductDto.class)).toList());
        productsResponseDto.setPageNumber(productPage.getNumber() + 1);
        productsResponseDto.setPageSize(productPage.getSize());
        productsResponseDto.setTotalPages(productPage.getTotalPages());
        productsResponseDto.setTotalElements(productPage.getTotalElements());
        productsResponseDto.setLastPage(productPage.isLast());
        return productsResponseDto;
    }

    public ProductsResponseDto getProductsByCategory(Long categoryId,Integer pageSize,Integer pageNumber,String sortBy,String sortDir){
        Sort sortByAndDir = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber - 1, pageSize,sortByAndDir);

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("categoryId",
                "Not Found",categoryId));
        Page<Product> productPage = productRepository.findAllByCategory(category,pageDetails);
        if(productPage.getTotalElements() == 0) throw new APIException("No such Product present in this category");
        List<Product> products = productPage.getContent();
        ProductsResponseDto productsResponseDto = new ProductsResponseDto();
        productsResponseDto.setProducts(products.stream().map(product -> mapper.map(product, ProductDto.class)).toList());
        productsResponseDto.setPageNumber(productPage.getNumber() + 1);
        productsResponseDto.setPageSize(productPage.getSize());
        productsResponseDto.setTotalPages(productPage.getTotalPages());
        productsResponseDto.setTotalElements(productPage.getTotalElements());
        productsResponseDto.setLastPage(productPage.isLast());
        return productsResponseDto;
    }

    public ProductsResponseDto getProductsByKeyword(String keyword,Integer pageSize, Integer pageNumber,String sortBy,String sortDir){
        Sort sortByAndDir = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber - 1, pageSize, sortByAndDir);

        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCaseOrDescriptionIgnoreCase(keyword,keyword,pageDetails);
        if(productPage.getTotalElements() ==0 && productPage.getTotalPages() == 0) throw new APIException("No product is present of that keyword");
        List<Product> products = productPage.getContent();
        ProductsResponseDto productsResponseDto = new ProductsResponseDto();
        productsResponseDto.setProducts(products.stream().map(product -> mapper.map(product, ProductDto.class)).toList());
        productsResponseDto.setPageNumber(productPage.getNumber() + 1);
        productsResponseDto.setPageSize(productPage.getSize());
        productsResponseDto.setTotalPages(productPage.getTotalPages());
        productsResponseDto.setTotalElements(productPage.getTotalElements());
        productsResponseDto.setLastPage(productPage.isLast());
        return productsResponseDto;
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

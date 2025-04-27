package org.utkrisht.product.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utkrisht.product.config.AppConstants;
import org.utkrisht.product.dto.CategoriesResponseDto;
import org.utkrisht.product.dto.CategoryDto;
import org.utkrisht.product.model.Category;
import org.utkrisht.product.service.CategoryService;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/category")
    public ResponseEntity<CategoriesResponseDto> getAllCategories(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                  @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                  @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_BY) String sortyBy,
                                                                  @RequestParam(name="sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir){

        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize,sortyBy,sortDir), HttpStatus.OK);
    }


    @PostMapping("/api/admin/category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto){

        return new ResponseEntity<>(categoryService.saveCategory(dto),HttpStatus.CREATED);
    }

    @GetMapping("/api/public/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long categoryId){
        Category category = categoryService.getCategoryById(categoryId);

        if(category == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/category/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable("id") Long categoryId){
        var deletedCategory = categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(deletedCategory,HttpStatus.OK);
    }

    @PutMapping("/api/admin/category/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long categoryId,@Valid @RequestBody CategoryDto dto){

        Category updatedCategory = categoryService.updateCategoryById(categoryId,dto);

        return new ResponseEntity<>("/api/public/category/" + updatedCategory.getId(), HttpStatus.OK);
    }
}

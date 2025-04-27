package org.utkrisht.product.mapper;

import org.utkrisht.product.dto.CategoryDto;
import org.utkrisht.product.model.Category;

public class CategoryMapper {
    public static Category dtoToEntity(CategoryDto dto){
        Category category = new Category();
        category.setTitle(dto.getTitle());
        category.setDescription(dto.getDescription());

        return category;

    }
}

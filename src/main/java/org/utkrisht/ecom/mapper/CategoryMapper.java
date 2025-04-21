package org.utkrisht.ecom.mapper;

import org.utkrisht.ecom.dto.CategoryDto;
import org.utkrisht.ecom.model.Category;

public class CategoryMapper {
    public static Category dtoToEntity(CategoryDto dto){
        Category category = new Category();
        category.setTitle(dto.getTitle());
        category.setDescription(dto.getDescription());

        return category;

    }
}

package org.utkrisht.ecom.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.utkrisht.ecom.dto.CategoriesResponseDto;
import org.utkrisht.ecom.dto.CategoryDto;
import org.utkrisht.ecom.exceptions.APIException;
import org.utkrisht.ecom.exceptions.ResourceNotFoundException;
import org.utkrisht.ecom.model.Category;
import org.utkrisht.ecom.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

//    private final List<Category> categories = new ArrayList <>();
//    private Long id = 1L;
    private final ModelMapper mapper;
    private final CategoryRepository categoryRepository;

    public CategoryService(ModelMapper mapper, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    public CategoriesResponseDto getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortDir){

        Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber - 1,pageSize,sortByAndOrder); //pageNumber is zero indexed
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
//        long totalCategory = categoryRepository.count();
        if(categoryPage.getTotalElements() == 0) throw new APIException("No categories created Yet");
        List<CategoryDto> categoriesDtos =categories.stream().map(category -> mapper.map(category,CategoryDto.class)).toList();

        var response = new CategoriesResponseDto();
//        int totalPage = (int) Math.ceil((double) totalCategory /pageSize);
        response.setContent(categoriesDtos);
        response.setPageNumber(categoryPage.getNumber() + 1);
        response.setPageSize(categoryPage.getSize());
        response.setTotalPages(categoryPage.getTotalPages());
        response.setTotalElements(categoryPage.getTotalElements());
        response.setLastPage(categoryPage.isLast());
        return response;
    }

    public CategoryDto saveCategory(CategoryDto dto){
//        category.setId(id++);
//        categories.add(category);
        Optional<Category> op = categoryRepository.findByTitle(dto.getTitle());
        if(op.isPresent()){
            throw new APIException(String.format("Category %s already present",dto.getTitle()));
        }
//        Category category = CategoryMapper.dtoToEntity(dto);
        Category category = mapper.map(dto,Category.class);
        Category savedCategory = categoryRepository.save(category);

        return mapper.map(savedCategory,CategoryDto.class);
    }

    public Category getCategoryById(Long id){

        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id","this id is not present in it",id));
//        return categories.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public CategoryDto deleteCategoryById(Long id){
//       Optional<Category> category = categories.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
        Optional<Category> opCategory = categoryRepository.findById(id);
        if(opCategory.isEmpty()) throw new ResourceNotFoundException("CategoryId","No such category for that id",id);
        categoryRepository.deleteById(id);
        return mapper.map(opCategory.get(),CategoryDto.class);
//        category.ifPresent(categories::remove);
    }

    public Category updateCategoryById(Long id,CategoryDto category){
//        Optional<Category> categoryOptional = categories.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();

       Category findedCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category","No such Category id present",id));
            findedCategory.setTitle(category.getTitle());
            findedCategory.setDescription(category.getDescription());

          return  categoryRepository.save(findedCategory);

    }
}

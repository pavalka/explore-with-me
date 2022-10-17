package ru.practicum.explorewithme.services.categories;

import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategoriesPageable(long from, int size);

    CategoryDto getCategory(long categoryId);

    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(long categoryId);
}

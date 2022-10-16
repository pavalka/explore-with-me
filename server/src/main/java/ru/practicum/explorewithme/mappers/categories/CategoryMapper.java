package ru.practicum.explorewithme.mappers.categories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.entities.categories.Category;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CategoryMapper {
    public static CategoryDto mapToDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(category.getId(), category.getName());
    }

    public static List<CategoryDto> mapToDto(Collection<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream().map(CategoryMapper::mapToDto).collect(Collectors.toList());
    }

    public static Category mapFromDto(NewCategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return new Category(categoryDto.getName());
    }
}

package ru.practicum.explorewithme.services.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.entities.categories.Category;
import ru.practicum.explorewithme.exceptions.categories.CategoryNotFoundException;
import ru.practicum.explorewithme.exceptions.categories.DeletingNonEmptyCategoryException;
import ru.practicum.explorewithme.mappers.categories.CategoryMapper;
import ru.practicum.explorewithme.pageable.PageableByOffsetAndSize;
import ru.practicum.explorewithme.repositories.categories.CategoryRepository;
import ru.practicum.explorewithme.services.events.ThirdPartEventService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService, ThirdPartCategoryService {
    private final CategoryRepository categoryRepository;
    private final ThirdPartEventService eventService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, @Lazy ThirdPartEventService eventService) {
        this.categoryRepository = categoryRepository;
        this.eventService = eventService;
    }

    @Override
    public List<CategoryDto> getAllCategoriesPageable(long from, int size) {
        var pageable = new PageableByOffsetAndSize(from, size, Sort.by(Sort.Direction.ASC, "id"));

        return CategoryMapper
                .mapToDto(categoryRepository
                        .findAll(pageable)
                        .toList()
                );
    }

    @Override
    public CategoryDto getCategory(long categoryId) {
        return CategoryMapper
                .mapToDto(getCategoryById(categoryId));
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        var category = CategoryMapper.mapFromDto(categoryDto);

        return CategoryMapper
                .mapToDto(categoryRepository
                        .save(category)
                );
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        var category = getCategoryById(categoryDto.getId());

        category.setName(categoryDto.getName());
        return CategoryMapper.mapToDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(long categoryId) {
        var category = getCategoryById(categoryId);

        if (eventService.isCategoryUsed(category)) {
            throw new DeletingNonEmptyCategoryException(categoryId);
        }

        categoryRepository.delete(category);
    }

    @Override
    public Category getCategoryById(long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}

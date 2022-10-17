package ru.practicum.explorewithme.controllers.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.services.categories.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> getAllCategoriesPageable(@RequestParam(name = "from", defaultValue = "0")
                                                            @PositiveOrZero long from,
                                                            @RequestParam(name = "size", defaultValue = "10")
                                                            @Positive int size) {
        return categoryService.getAllCategoriesPageable(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") long categoryId) {
        return categoryService.getCategory(categoryId);
    }
}

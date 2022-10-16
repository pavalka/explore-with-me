package ru.practicum.explorewithme.repositories.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.entities.categories.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

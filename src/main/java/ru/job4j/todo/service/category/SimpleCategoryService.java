package ru.job4j.todo.service.category;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.category.CategoryRepository;
import java.util.Collection;

@Service
public class SimpleCategoryService implements CategoryService {
    private CategoryRepository categoryRepository;

    public SimpleCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable("categories")
    public Collection<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Collection<Category> findAllInRange(Collection<Integer> indexRange) {
        return categoryRepository.findAllInRange(indexRange);
    }
}

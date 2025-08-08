package ru.job4j.todo.repository.priority;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class HibernatePriorityRepository implements PriorityRepository {
    private final CrudRepository crudRepository;

    public HibernatePriorityRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<Priority> findByName(String name) {
        return crudRepository.optional("from Priority where name = :name", Priority.class, Map.of("name", name));
    }

    @Override
    public Collection<Priority> findAll() {
        return crudRepository.query("from Priority", Priority.class);
    }
}

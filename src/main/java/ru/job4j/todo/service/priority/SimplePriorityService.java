package ru.job4j.todo.service.priority;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.priority.PriorityRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimplePriorityService implements PriorityService {
    private final PriorityRepository priorityRepository;

    public SimplePriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @Override
    public Optional<Priority> findByName(String name) {
        var priority = priorityRepository.findByName(name);
        if (priority.isEmpty()) {
            throw new IllegalArgumentException("No priority found with name: " + name);
        }
        return priority;
    }

    @Override
    @Cacheable("priorities")
    public Collection<Priority> findAll() {
        return priorityRepository.findAll();
    }
}

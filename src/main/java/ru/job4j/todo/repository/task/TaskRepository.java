package ru.job4j.todo.repository.task;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);

    boolean deleteById(int id);

    void update(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();
}

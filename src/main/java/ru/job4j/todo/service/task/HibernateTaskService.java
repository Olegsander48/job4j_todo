package ru.job4j.todo.service.task;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.task.TaskRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class HibernateTaskService implements TaskService {
    private TaskRepository taskRepository;

    public HibernateTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        if (task.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        return taskRepository.save(task);
    }

    @Override
    public boolean deleteById(int id) {
        boolean success = taskRepository.deleteById(id);
        if (!success) {
            throw new NoSuchElementException("Task with id " + id + " not found");
        }
        return success;
    }

    @Override
    public void update(Task task) {
        if (task.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        taskRepository.update(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new NoSuchElementException("No task with id " + id + " found");
        }
        return optionalTask;
    }

    @Override
    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }
}

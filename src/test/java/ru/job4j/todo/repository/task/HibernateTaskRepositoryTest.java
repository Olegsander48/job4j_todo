package ru.job4j.todo.repository.task;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.configuration.HibernateConfiguration;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;

class HibernateTaskRepositoryTest {
    private static HibernateTaskRepository hibernateTaskRepository;

    @BeforeAll
    static void initRepositories() {
        var configuration = new HibernateConfiguration();
        SessionFactory sessionFactory = configuration.sf();

        hibernateTaskRepository = new HibernateTaskRepository(sessionFactory);
    }

    @AfterEach
    void clearTasks() {
        var tasks = hibernateTaskRepository.findAll();
        for (var task : tasks) {
            hibernateTaskRepository.deleteById(task.getId());
        }
    }

    /**
     * Test case: Save a task to the database and verify that the same task can be retrieved.
     */
    @Test
    void whenSaveThenGetSame() {
        var task = hibernateTaskRepository.save(new Task(1, "test description", LocalDateTime.now(), true));
        var savedTask = hibernateTaskRepository.findById(task.getId());
        assertThat(savedTask).contains(task);
    }

    /**
     * Test case: Save three tasks to the database and verify that the same tasks can be retrieved.
     */
    @Test
    void whenSaveSeveralThenGetAll() {
        var task1 = hibernateTaskRepository.save(new Task(1, "test description1", LocalDateTime.now(), true));
        var task2 = hibernateTaskRepository.save(new Task(2, "test description2", LocalDateTime.now(), true));
        var task3 = hibernateTaskRepository.save(new Task(3, "test description3", LocalDateTime.now(), true));
        var result = hibernateTaskRepository.findAll();
        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    /**
     * Test case: Save 0 tasks to the database and verify that there is no tasks.
     */
    @Test
    void whenDontSaveThenNothingFound() {
        assertThat(hibernateTaskRepository.findAll()).isEqualTo(emptyList());
        assertThat(hibernateTaskRepository.findById(0)).isEmpty();
    }

    /**
     * Test case: Save and delete a task to the database and verify that the database is empty
     */
    @Test
    void whenDeleteThenGetEmptyOptional() {
        var task = hibernateTaskRepository.save(new Task(1, "test description", LocalDateTime.now(), true));
        var isDeleted = hibernateTaskRepository.deleteById(task.getId());
        var savedTask = hibernateTaskRepository.findById(task.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTask).isEmpty();
    }

    /**
     * Test case: Delete a task from empty database and verify that it is impossible
     */
    @Test
    void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(hibernateTaskRepository.deleteById(0)).isFalse();
    }

    /**
     * Test case: Update saved task and verify that the task is updated
     */
    @Test
    void whenUpdateThenGetUpdated() {
        var task = hibernateTaskRepository.save(new Task(1, "test description", LocalDateTime.now(), true));
        var updatedTask = new Task(task.getId(), "updated test description", task.getCreated(), false);
        hibernateTaskRepository.update(updatedTask);
        var savedTask = hibernateTaskRepository.findById(task.getId());
        assertThat(savedTask).contains(updatedTask);
    }

}
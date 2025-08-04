package ru.job4j.todo.repository.task;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.configuration.HibernateConfiguration;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.CrudRepository;
import ru.job4j.todo.repository.user.HibernateUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class HibernateTaskRepositoryTest {
    private static HibernateTaskRepository hibernateTaskRepository;
    private static HibernateUserRepository hibernateUserRepository;
    private static CrudRepository crudRepository;
    private static User user;
    private static Priority priority;

    @BeforeAll
    static void initRepositories() {
        var configuration = new HibernateConfiguration();
        SessionFactory sessionFactory = configuration.sf();
        crudRepository = new CrudRepository(sessionFactory);

        hibernateUserRepository = new HibernateUserRepository(crudRepository);
        hibernateTaskRepository = new HibernateTaskRepository(crudRepository);

        user = hibernateUserRepository.save(new User()).get();

        var priorities = crudRepository.query("from Priority", Priority.class);
        priority = priorities.isEmpty() ? new Priority() : priorities.get(0);

    }

    @AfterEach
    void clearTasks() {
        var tasks = hibernateTaskRepository.findAll();
        for (var task : tasks) {
            hibernateTaskRepository.deleteById(task.getId());
        }
    }

    @AfterAll
    static void clearUserRepository() {
        hibernateUserRepository.deleteById(user.getId());
    }

    /**
     * Helper method to get priority by name from database
     */
    private Priority getPriorityByName(String name) {
        var priorities = crudRepository.query("from Priority p where p.name = :name", Priority.class, Map.of("name", name));
        return priorities.isEmpty() ? new Priority() : priorities.get(0);
    }

    /**
     * Test case: Save a task to the database and verify that the same task can be retrieved.
     */
    @Test
    void whenSaveThenGetSame() {
        // You can also use specific priority by name if needed
        var urgentPriority = getPriorityByName("urgently");
        var task = hibernateTaskRepository.save(new Task(1, "test description", LocalDateTime.now(), true, user, urgentPriority));
        var savedTask = hibernateTaskRepository.findById(task.getId());
        assertThat(savedTask).contains(task);
    }

    /**
     * Test case: Save three tasks to the database and verify that the same tasks can be retrieved.
     */
    @Test
    void whenSaveSeveralThenGetAll() {
        var task1 = hibernateTaskRepository.save(new Task(1, "test description1", LocalDateTime.now(), true, user, priority));
        var task2 = hibernateTaskRepository.save(new Task(2, "test description2", LocalDateTime.now(), true, user, priority));
        var task3 = hibernateTaskRepository.save(new Task(3, "test description3", LocalDateTime.now(), true, user, priority));
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
        var task = hibernateTaskRepository.save(new Task(1, "test description", LocalDateTime.now(), true, user, priority));
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
        var task = hibernateTaskRepository.save(new Task(1, "test description", LocalDateTime.now(), true, user, priority));
        var updatedTask = new Task(task.getId(), "updated test description", task.getCreated(), false, user, priority);
        hibernateTaskRepository.update(updatedTask);
        var savedTask = hibernateTaskRepository.findById(task.getId());
        assertThat(savedTask).contains(updatedTask);
    }

}
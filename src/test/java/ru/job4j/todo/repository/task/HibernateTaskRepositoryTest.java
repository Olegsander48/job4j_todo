package ru.job4j.todo.repository.task;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.job4j.todo.configuration.HibernateConfiguration;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.CrudRepository;
import ru.job4j.todo.repository.user.HibernateUserRepository;
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

        priority = crudRepository.query("from Priority where id = :fid", Priority.class, Map.of("fid", 1)).get(0);
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
     * Test case: Save a task to the database and verify that the same task can be retrieved.
     */
    @Test
    void whenSaveThenGetSame() {
        // You can also use specific priority by name if needed

        var task = hibernateTaskRepository.save(new Task("test description", user, priority, List.of()));
        var savedTask = hibernateTaskRepository.findById(task.getId());
        assertThat(savedTask).contains(task);
    }

    /**
     * Test case: Save three tasks to the database and verify that the same tasks can be retrieved.
     */
    @Test
    void whenSaveSeveralThenGetAll() {
        var task1 = hibernateTaskRepository.save(new Task("test description1", user, priority, List.of()));
        var task2 = hibernateTaskRepository.save(new Task("test description2", user, priority, List.of()));
        var task3 = hibernateTaskRepository.save(new Task("test description3", user, priority, List.of()));
        var result = crudRepository.query("from Task t JOIN FETCH t.priority JOIN FETCH t.user LEFT JOIN FETCH t.categories", Task.class);
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
        var task = hibernateTaskRepository.save(new Task("test description", user, priority, List.of()));
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
        var task = hibernateTaskRepository.save(new Task("test description", user, priority, List.of()));
        var updatedTask = new Task("updated test description",  user, priority, List.of());
        updatedTask.setId(task.getId());
        hibernateTaskRepository.update(updatedTask);
        var savedTask = hibernateTaskRepository.findById(task.getId());
        assertThat(savedTask).contains(updatedTask);
    }

}
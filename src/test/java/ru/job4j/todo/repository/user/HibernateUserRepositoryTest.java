package ru.job4j.todo.repository.user;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.configuration.HibernateConfiguration;
import ru.job4j.todo.model.User;

import java.util.List;

class HibernateUserRepositoryTest {
    private static HibernateUserRepository hibernateUserRepository;

    @BeforeAll
    static void init() {
        var configuration = new HibernateConfiguration();
        SessionFactory sessionFactory = configuration.sf();

        hibernateUserRepository = new HibernateUserRepository(sessionFactory);
    }

    @AfterEach
    void clearUsers() {
        var users = hibernateUserRepository.findAll();
        for (var user : users) {
            hibernateUserRepository.deleteById(user.getId());
        }
    }

    /**
     * Test case: Save a user to the database and verify that the same user can be retrieved.
     */
    @Test
    void whenSaveThenGetSame() {
        var user = hibernateUserRepository.save(new User(0, "Aleks Prig", "google@gmail.com", "1234"));
        var savedUser = hibernateUserRepository.findById(user.get().getId());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    /**
     * Test case: Save three users to the database and verify that the same users can be retrieved.
     */
    @Test
    void whenSaveSeveralThenGetAll() {
        var user1 = hibernateUserRepository.save(new User(0, "Aleks Prig1", "google1@gmail.com",  "1234")).get();
        var user2 = hibernateUserRepository.save(new User(0, "Aleks Prig2", "google2@gmail.com", "1234")).get();
        var user3 = hibernateUserRepository.save(new User(0, "Aleks Prig3", "google3@gmail.com", "1234")).get();
        var result = hibernateUserRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1, user2, user3));
    }

    /**
     * Test case: Save 0 users to the database and verify that there is no users.
     */
    @Test
    void whenDontSaveThenNothingFound() {
        assertThat(hibernateUserRepository.findAll()).isEqualTo(emptyList());
        assertThat(hibernateUserRepository.findById(0)).isEmpty();
    }

    /**
     * Test case: Save and delete a user to the database and verify that the database is empty
     */
    @Test
    void whenDeleteThenGetEmptyOptional() {
        var user = hibernateUserRepository.save(new User(0, "Aleks Prig", "google@gmail.com", "1234"));
        var isDeleted = hibernateUserRepository.deleteById(user.get().getId());
        var savedUser = hibernateUserRepository.findById(user.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedUser).isEmpty();
    }

    /**
     * Test case: Delete a user from empty database and verify that it is impossible
     */
    @Test
    void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(hibernateUserRepository.deleteById(0)).isFalse();
    }

    /**
     * Test case: Attempt to save a user with the same email, name, and password as an existing user,
     * and verify that an exception is thrown.
     */
    @Test
    void whenPutExistingUserThenGetException() {
        hibernateUserRepository.save(new User(0, "Aleks Prig", "google@gmail.com", "1234"));
        assertThatThrownBy(() -> hibernateUserRepository.save(new User(0, "Aleks Prig", "google@gmail.com", "1234")))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("could not execute statement");
    }

    /**
     * Test case: Attempt to save a user with the same email as an existing user,
     * and verify that an exception is thrown.
     */
    @Test
    void whenPutExistingEmailThenGetException() {
        hibernateUserRepository.save(new User(0, "Aleks Prig", "google@gmail.com", "1234"));
        assertThatThrownBy(() -> hibernateUserRepository.save(new User(0, "Aleks Prig", "google@gmail.com", "1234")))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("could not execute statement");
    }

    /**
     * Test case: Try to find saved user by email and password and verify that found user the same as saved
     */
    @Test
    void whenTryToFindByEmailAndPasswordThenGetUser() {
        var user = hibernateUserRepository.save(new User(0, "Aleks Prig", "google@gmail.com", "1234"));
        assertThat(hibernateUserRepository.findByLoginAndPassword(user.get().getLogin(), user.get().getPassword())).isEqualTo(user);
    }

    /**
     * Test case: Try to find not saved user by email and password and verify that there is no such user
     */
    @Test
    void whenTryToFindNonExistingUserByEmailAndPasswordThenGetUser() {
        var user = new User(0, "Aleks Prig", "google@gmail.com", "1234");
        assertThat(hibernateUserRepository.findByLoginAndPassword(user.getLogin(), user.getPassword())).isEmpty();
    }
}
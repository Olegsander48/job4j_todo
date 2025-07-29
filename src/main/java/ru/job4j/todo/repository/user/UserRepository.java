package ru.job4j.todo.repository.user;

import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Optional<User> save(User user);

    Optional<User> findByLoginAndPassword(String login, String password);

    Collection<User> findAll();

    Optional<User> findById(int id);

    boolean deleteById(int id);
}

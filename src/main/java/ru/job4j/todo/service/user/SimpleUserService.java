package ru.job4j.todo.service.user;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.user.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> save(User user) {
        if (userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> user = userRepository.findByLoginAndPassword(login, password);
        if (user.isEmpty()) {
            throw new NoSuchElementException("Login or password is incorrect");
        }
        return user;
    }
}

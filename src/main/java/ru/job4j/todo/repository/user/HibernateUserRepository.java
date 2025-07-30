package ru.job4j.todo.repository.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        crudRepository.run(session -> session.save(user));
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", user.getId()));
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User where login = :fLogin and password = :fPassword", User.class,
                Map.of("fLogin", login, "fPassword", password));
    }

    @Override
    public Collection<User> findAll() {
        return crudRepository.query("from User", User.class);
    }

    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", id));
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.executeUpdate(
                "delete from User where id = :fid",
                Map.of("fid", id));
    }
}

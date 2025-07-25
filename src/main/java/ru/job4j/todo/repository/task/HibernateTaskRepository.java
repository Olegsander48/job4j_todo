package ru.job4j.todo.repository.task;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public Task save(Task task) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        Task task;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            task = session.get(Task.class, id);
            if (task != null) {
                session.delete(task);
            }
            session.getTransaction().commit();
        }
        return task != null;
    }

    @Override
    public void update(Task task) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.merge(task);
            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        Task task;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            task = session.get(Task.class, id);
            session.getTransaction().commit();
        }
        return Optional.ofNullable(task);
    }

    @Override
    public Collection<Task> findAll() {
        List<Task> tasks;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            tasks = session.createQuery("from Task").list();
            session.getTransaction().commit();
        }
        return tasks;
    }
}

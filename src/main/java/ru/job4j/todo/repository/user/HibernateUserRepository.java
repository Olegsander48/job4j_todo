package ru.job4j.todo.repository.user;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateUserRepository implements UserRepository {
    private SessionFactory sessionFactory;

    public HibernateUserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> save(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> user;
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            user = session.createQuery("from User where login = :login and password = :password")
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .stream().findFirst();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public Collection<User> findAll() {
        List<User> users;
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            users = session.createQuery("from User").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public Optional<User> findById(int id) {
        User user;
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean deleteById(int id) {
        User user;
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return user != null;
    }
}

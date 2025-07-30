package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class CrudRepository {
    private SessionFactory sessionFactory;

    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    public boolean executeUpdate(String query, Map<String, Object> args) {
        Function<Session, Boolean> command = session -> {
            var sql = session.createQuery(query);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                sql.setParameter(entry.getKey(), entry.getValue());
            }
            return sql.executeUpdate() != 0;
        };
        return tx(command);
    }

    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sql = session.createQuery(query, cl);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                sql.setParameter(entry.getKey(), entry.getValue());
            }
            return sql.uniqueResultOptional();
        };
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session ->
                session.createQuery(query, cl).getResultList();
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sql = session.createQuery(query, cl);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                sql.setParameter(entry.getKey(), entry.getValue());
            }
            return sql.list();
        };
        return tx(command);
    }

    public <T> T tx(Function<Session, T> command) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}

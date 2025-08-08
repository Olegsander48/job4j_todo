package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    @EqualsAndHashCode.Exclude
    private LocalDateTime created = LocalDateTime.now();
    @EqualsAndHashCode.Exclude
    private boolean done;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "task_category",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    public Task(String description, User user, Priority priority, List<Category> categories) {
        this.description = description;
        this.user = user;
        this.priority = priority;
        this.categories = categories;
    }

    public String getFormattedCreationDate() {
        return created.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }
}

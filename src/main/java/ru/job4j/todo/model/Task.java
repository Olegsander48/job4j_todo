package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public String getFormattedCreationDate() {
        return created.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }
}

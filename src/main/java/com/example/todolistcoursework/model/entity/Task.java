package com.example.todolistcoursework.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "task")
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String data;
    @Column
    private boolean checkbox;
    @Column
    private LocalDateTime deadline;
    @Column
    private LocalDateTime created;
    @Column
    private LocalDateTime modified;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public void tick() {
        checkbox = !checkbox;
        modified = LocalDateTime.now();
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return checkbox == task.checkbox && data.equals(task.data) && deadline.equals(task.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, checkbox, deadline);
    }
}

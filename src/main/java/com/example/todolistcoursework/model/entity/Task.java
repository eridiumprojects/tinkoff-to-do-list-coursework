package com.example.todolistcoursework.model.entity;

import com.example.todolistcoursework.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Table(name = "task")
@Builder
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String data;
    @Column
    private LocalDateTime deadline;
    @Column
    private LocalDateTime created;
    @Column
    private String description;
    @Column
    private TaskStatus status;
    @Column
    private LocalDateTime modified;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {}

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(data, task.data) &&
                Objects.equals(deadline, task.deadline) && Objects.equals(created, task.created) &&
                Objects.equals(description, task.description) && status == task.status &&
                Objects.equals(modified, task.modified) && Objects.equals(user, task.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, deadline, created, description, status, modified, user);
    }
}

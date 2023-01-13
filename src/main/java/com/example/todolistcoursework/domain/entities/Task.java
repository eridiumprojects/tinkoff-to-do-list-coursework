package com.example.todolistcoursework.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity(name = "task")
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "data")
    private String data;
    @Column(name = "checkbox")
    private boolean checkbox;
    @Column(name = "deadline")
    private String deadline;

//    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
//    @JoinColumn(name = "user_id")
//    private User user;

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

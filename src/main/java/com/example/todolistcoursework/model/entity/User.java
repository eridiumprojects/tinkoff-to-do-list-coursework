package com.example.todolistcoursework.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "user")
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    @NotBlank
    private String username;
    @Column
    @NotBlank
    private String password;
    @Column
    @NotBlank
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<RefreshToken> tokens = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Device> devices = new ArrayList<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username) && password.equals(user.password) &&
                email.equals(user.email) && roles.equals(user.roles) && Objects.equals(tasks, user.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, roles, tasks);
    }
}

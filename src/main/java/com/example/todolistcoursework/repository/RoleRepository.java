package com.example.todolistcoursework.repository;

import com.example.todolistcoursework.model.entity.Role;
import com.example.todolistcoursework.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}

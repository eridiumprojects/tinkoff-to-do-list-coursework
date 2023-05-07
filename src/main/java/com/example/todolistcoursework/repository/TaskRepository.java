package com.example.todolistcoursework.repository;

import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, PagingAndSortingRepository<Task, Long> {
    Optional<Task> findById(Long id);

    void deleteById(Long id);

    Page<Task> findTasksByUserId(Long userId, Pageable page);
    Page<Task> findTasksByUserIdAndStatusIn(Long userId, List<TaskStatus> status, Pageable page);
}

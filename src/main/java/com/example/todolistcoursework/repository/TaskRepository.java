package com.example.todolistcoursework.repository;

import com.example.todolistcoursework.model.entity.Task;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(@NonNull Long id);

    void deleteById(@NonNull Long id);

    List<Task> findAllByData(String data);

    @Query(value = "SELECT t FROM task t WHERE t.data = :pData ORDER BY t.created DESC")
    List<Task> findAllByDataOrderQuery(@Param("pData") String data);

    List<Task> findAllByCheckbox(Boolean checkbox);

    @Query(value = "SELECT t FROM task t WHERE t.checkbox = :pCheckbox ORDER BY t.created DESC")
    List<Task> findAllByCheckboxOrderQuery(@Param("pCheckbox") Boolean checkbox);

    @Query(value = "SELECT t FROM task t WHERE t.checkbox = false ORDER BY t.deadline ASC")
    List<Task> findAllByOrderByCreated();

    @Query(value = "SELECT t FROM task t WHERE t.checkbox = true")
    List<Task> findAllCompletedTasks();
}

package com.example.todolistcoursework.service;

import com.example.todolistcoursework.builder.TaskMapper;
import com.example.todolistcoursework.model.dto.request.FilterRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.enums.TaskStatus;
import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.model.exception.ClientException;
import com.example.todolistcoursework.repository.TaskRepository;
import com.example.todolistcoursework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.todolistcoursework.model.constant.AuthErrorMessages.USER_NOT_FOUND;
import static com.example.todolistcoursework.model.constant.ClientErrorMessages.USER_DOESNT_HAVE_CURRENT_TASK;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    @Value("${pageable.size}")
    private Integer pageSize;

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_NOT_FOUND);
        }
        return user.get();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TaskInfo createTask(Long userId, Task task) {
        User user = getUser(userId);
        task.setUser(user);
        task.setCreated(task.getCreated());
        taskRepository.save(task);
        return TaskMapper.toApi(task);
    }

    public TaskInfo getTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            return TaskMapper.toApi(task.get());
        } else {
            throw new ClientException(USER_DOESNT_HAVE_CURRENT_TASK);
        }
    }

    public List<TaskInfo> getTasks(Long userId, Integer page) {
        if (page == null || page < 0) page = 0;
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("created").descending());
        var pageResponse = taskRepository.findTasksByUserId(userId, pageRequest);
        return pageResponse.stream()
                .sorted(Comparator.comparingInt(a -> TaskStatus.getPriority(a.getStatus())))
                .map(TaskMapper::toApi).toList();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TaskInfo updateTask(Long userId, Task request) {
        Optional<Task> task = taskRepository.findById(request.getId());
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            Task existingTask = task.get();
            existingTask.setData(request.getData());
            existingTask.setModified(LocalDateTime.now());
            existingTask.setDescription(request.getDescription());
            existingTask.setStatus(request.getStatus());
            var result = taskRepository.save(existingTask);
            return TaskMapper.toApi(result);
        } else {
            throw new ClientException(USER_DOESNT_HAVE_CURRENT_TASK);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TaskInfo deleteTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            taskRepository.deleteById(id);
            return TaskMapper.toApi(task.get());
        } else {
            throw new ClientException(USER_DOESNT_HAVE_CURRENT_TASK);
        }
    }

    public List<TaskInfo> filterTasks(Long userId, FilterRequest filterRequest) {
        var pageNumber = filterRequest.getPage();
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var statuses = filterRequest.getNecessaryStatuses();
        if (statuses == null || statuses.size() == 0) {
            statuses = List.of(TaskStatus.values());
        }

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("created").descending());
        var tasks = taskRepository.findTasksByUserIdAndStatusIn(userId, statuses, pageRequest);

        return TaskFilter.filter(filterRequest, tasks.stream().toList());
    }
}

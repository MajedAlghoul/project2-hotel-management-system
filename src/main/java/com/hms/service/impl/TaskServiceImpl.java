package com.hms.service.impl;

import com.hms.dto.TaskDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.NoContentException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Task;
import com.hms.model.Role;
import com.hms.repository.EmployeeRepository;
import com.hms.repository.TaskRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    public TaskDto postTask(TaskDto task) {
        if(taskRepository.existsByName(task.getName()))
            throw new DuplicateResourceException("Task", "task_id", task.getName());
        Task newTask = mapToEntity(task);
        //Role role = roleRepository.findByName("ROLE_EMPLOYEE").get();
        //newTask.setRole(Collections.singleton(role));
        //taskRepository.save(newTask);
        return mapToDto(taskRepository.save(newTask));
    }

    @Override
    public List<TaskDto> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new NoContentException("No tasks registered yet");
        }
        return tasks.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto replaceTask(TaskDto task) {
        if(!taskRepository.existsById(task.getId()))
            throw new ResourceNotFoundException("Task", "task_id", String.valueOf(task.getName()));
        return mapToDto(taskRepository.save(mapToEntity(task)));
    }

    @Override
    public TaskDto modifyTask(TaskDto partialTask) {
        Task originalTask = taskRepository.findById(partialTask.getId()).orElseThrow(() -> new ResourceNotFoundException("Task", "email", String.valueOf(partialTask.getId())));
        if(!partialTask.getName().isEmpty())
            originalTask.setId(partialTask.getId());
        if(!partialTask.getName().isEmpty())
            originalTask.setName(partialTask.getName());
        taskRepository.save(originalTask);
        return mapToDto(originalTask);
    }


    @Override
    public void deleteTask(Long task_id) {
        if(!taskRepository.existsById(task_id))
            throw new ResourceNotFoundException("Task", "task_id", String.valueOf(task_id));
        taskRepository.deleteById(task_id);
    }

    @Override
    public TaskDto getTaskById(Long task_id) {
        return mapToDto(taskRepository.findById(task_id).orElseThrow(() -> new ResourceNotFoundException("Task", "task_id", String.valueOf(task_id))));
    }

    private TaskDto mapToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .employee(task.getAssignedTo().getEmail())
                .description(task.getDescription())
                .build();
    }

    private Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setAssignedTo(employeeRepository.findByEmail(taskDto.getEmployee()).get() );
        task.setDescription(taskDto.getDescription());
        return task;
    }
}

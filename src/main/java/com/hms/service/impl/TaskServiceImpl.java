package com.hms.service.impl;

import com.hms.dto.TaskDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Task;
import com.hms.model.Role;
import com.hms.repository.TaskRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
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
    public TaskDto[] getTasks(Integer pageNumber, Integer pageSize) {
        Object[] tasksObjects = taskRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        TaskDto[] tasksDtos = new TaskDto[tasksObjects.length];
        for(int i = 0; i < tasksObjects.length; i++)
            if (tasksObjects[i] instanceof Task)
                tasksDtos[i] = mapToDto((Task) tasksObjects[i]);
        if(tasksDtos.length == 0)
            throw new ResourceNotFoundException("Task", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return tasksDtos;
    }

    @Override
    public TaskDto replaceTask(TaskDto task) {
        if(!taskRepository.existsByName(task.getName()))
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
                .description(task.getDescription())
                .build();
    }

    private Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        return task;
    }
}

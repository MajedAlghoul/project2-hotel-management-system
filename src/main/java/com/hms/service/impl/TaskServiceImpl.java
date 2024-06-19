package com.hms.service.impl;

import com.hms.dto.TaskDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Task;
import com.hms.repository.TaskRepository;
import com.hms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDto postTask(TaskDto task) {
        if(taskRepository.existsById(task.getId()))
            throw new DuplicateResourceException("Task", "task_id", task.getName());
        return mapToDto(taskRepository.save(mapToEntity(task)));
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
        if(!taskRepository.existsById(task.getId()))
            throw new ResourceNotFoundException("Task", "task_id", String.valueOf(task.getId()));
        return mapToDto(taskRepository.save(mapToEntity(task)));
    }

    @Override
    public TaskDto modifyTask(TaskDto partialTask) {
        Task originalTask = taskRepository.findById(partialTask.getId()).orElseThrow(() -> new ResourceNotFoundException("Task", "task_id", String.valueOf(partialTask.getId())));
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
        return Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .build();
    }
}

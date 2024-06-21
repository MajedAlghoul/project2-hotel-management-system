package com.hms.service;

import com.hms.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto postTask(TaskDto task);
    List<TaskDto> getTasks();
    TaskDto replaceTask(TaskDto task);
    TaskDto modifyTask(TaskDto task);
    void deleteTask(Long task_id);
    TaskDto getTaskById(Long task_id);
}

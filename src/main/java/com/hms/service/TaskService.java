package com.hms.service;

import com.hms.dto.TaskDto;

public interface TaskService {
    TaskDto postTask(TaskDto task);
    TaskDto[] getTasks(Integer pageNumber, Integer pageSize);
    TaskDto replaceTask(TaskDto task);
    TaskDto modifyTask(TaskDto task);
    void deleteTask(String task_id);
    TaskDto getTaskById(String task_id);
}

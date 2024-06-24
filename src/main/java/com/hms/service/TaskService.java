package com.hms.service;

import com.hms.dto.TaskDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    TaskDto postTask(TaskDto task);
    Page<TaskDto> getTasks(int page,int size);
    TaskDto replaceTask(TaskDto task);
    TaskDto modifyTask(TaskDto task);
    void deleteTask(Long task_id);
    TaskDto getTaskById(Long task_id);
}

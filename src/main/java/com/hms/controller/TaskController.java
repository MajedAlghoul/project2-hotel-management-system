package com.hms.controller;

import com.hms.dto.TaskDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Task resource.
 */
@RequestMapping("/api/v2/task")
@RestController
@Validated
@Tag(name = "Task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            description = "Endpoint for fetching a list of Tasks",
            summary = "Fetch Tasks",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping(produces = "application/json")
    public ResponseEntity<TaskDto[]> getTasks(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Task", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Task", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(taskService.getTasks(pageNumberInt, pageSizeInt));
    }

    @Operation(
            description = "Endpoint for replacing an Task",
            summary = "Replace Task",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PutMapping(produces = "application/json")
    public ResponseEntity<TaskDto> putTask(@Validated @RequestBody TaskDto task) {
        return ResponseEntity.ok().body(taskService.replaceTask(task));
    }

    @Operation(
            description = "Endpoint for partially updating an Task",
            summary = "Partially update Task",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PatchMapping(produces = "application/json")
    public ResponseEntity<TaskDto> patchTask(@Validated @RequestBody TaskDto task) {
        return ResponseEntity.ok().body(taskService.modifyTask(task));
    }

    @Operation(
            description = "Endpoint for deleting an Task",
            summary = "Delete Task",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @DeleteMapping(value = "/{task_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteTask(@Validated @PathVariable String task_id) {
        long id;
        try{
            id = Integer.parseInt(task_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Task", "task_id", task_id);
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @Operation(
            description = "Endpoint for getting an Task",
            summary = "Get Task",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping(value = "/{task_id}", produces = "application/json")
    public ResponseEntity<TaskDto> getTask(@Validated @PathVariable String task_id) {
        long id;
        try{
            id = Integer.parseInt(task_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Task", "task_id", task_id);
        }
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }
}

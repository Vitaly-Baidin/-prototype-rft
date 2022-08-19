package com.render.server.controller;

import com.render.server.dto.request.TaskRequest;
import com.render.server.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${renderf.uri.task}", produces = "application/json")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getHistoryTaskByTask(@PathVariable Long id) {
        return taskService.getHistoryTaskByTask(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }


}

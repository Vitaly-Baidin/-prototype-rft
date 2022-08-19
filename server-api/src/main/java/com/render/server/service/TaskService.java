package com.render.server.service;

import com.render.server.domain.enums.Status;
import com.render.server.dto.request.TaskRequest;
import org.springframework.http.ResponseEntity;

public interface TaskService {

    ResponseEntity<?> createTask(TaskRequest request);

    ResponseEntity<?> getAllTasks();

    ResponseEntity<?> getTask(Long id);

    ResponseEntity<?> getHistoryTaskByTask(Long id);

    void addHistoryTaskInTask(Long id, Status status);

    ResponseEntity<?> deleteTask(Long id);
}

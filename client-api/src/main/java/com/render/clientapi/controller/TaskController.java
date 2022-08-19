package com.render.clientapi.controller;

import com.render.clientapi.dto.request.TaskRequest;
import com.render.clientapi.dto.response.CustomerResponse;
import com.render.clientapi.dto.response.TaskHistoryResponse;
import com.render.clientapi.dto.response.TaskResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "${renderf.uri.task}", produces = "application/json")
public class TaskController {

    @Value("${renderf.server.url}")
    private String serverUrl;

    @Value("${renderf.server.uri.task}")
    private String taskUri;

    private final RestTemplate restTemplate;

    public TaskController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequest request) {
        return restTemplate.postForEntity(serverUrl + taskUri, request, TaskResponse.class);
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        return restTemplate.getForEntity(serverUrl + taskUri, TaskResponse[].class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        return restTemplate.getForEntity(serverUrl + taskUri + "/" + id, TaskResponse.class);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getHistoryTaskByTask(@PathVariable Long id) {
        return restTemplate.getForEntity(serverUrl + taskUri + "/" + id + "/history", TaskHistoryResponse.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        restTemplate.delete(serverUrl + taskUri + "/" + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

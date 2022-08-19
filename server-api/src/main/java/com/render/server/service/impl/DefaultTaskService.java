package com.render.server.service.impl;

import com.render.server.domain.Customer;
import com.render.server.domain.Task;
import com.render.server.domain.TaskHistory;
import com.render.server.domain.enums.Status;
import com.render.server.dto.message.TaskMessage;
import com.render.server.dto.request.TaskRequest;
import com.render.server.dto.response.TaskHistoryResponse;
import com.render.server.dto.response.TaskResponse;
import com.render.server.repository.CustomerRepository;
import com.render.server.repository.TaskHistoryRepository;
import com.render.server.repository.TaskRepository;
import com.render.server.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DefaultTaskService implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    private final CustomerRepository customerRepository;

    private final RabbitTemplate rabbitTemplate;

    public DefaultTaskService(TaskRepository taskRepository,
                              TaskHistoryRepository taskHistoryRepository,
                              CustomerRepository customerRepository,
                              RabbitTemplate rabbitTemplate) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.customerRepository = customerRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ResponseEntity<?> createTask(TaskRequest request) {
        Optional<Customer> customerOptional = customerRepository.findByName(request.getCustomerName());

        if (customerOptional.isPresent() && !taskRepository.existsByName(request.getName())) {
            Task task = new Task();

            task.setName(request.getName());
            task.setCustomer(customerOptional.get());
            task.setStatus(Status.CREATE);

            task.setTaskHistories(createTaskHistory(task));

            rabbitTemplate.convertAndSend("render.task.request", createTaskMessage(task));

            receiveTaskMessage(createTaskMessage(task));

            taskRepository.save(task);

            return new ResponseEntity<>(createTaskResponse(task), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getAllTasks() {
        List<TaskResponse> tasks = createTaskResponseList(taskRepository.findAll());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            return new ResponseEntity<>(createTaskResponse(task.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> getHistoryTaskByTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            List<TaskHistory> taskHistories = taskHistoryRepository.findAllByTask(task.get());

            return new ResponseEntity<>(createTaskHistoryResponseList(taskHistories), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Override
    public void addHistoryTaskInTask(Long id, Status status) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            List<TaskHistory> taskHistories = task.getTaskHistories();
            TaskHistory taskHistory = new TaskHistory();

            taskHistory.setStatus(status);
            taskHistory.setDateTime(LocalDateTime.now());

            taskHistories.add(taskHistory);

            task.setTaskHistories(taskHistories);

            taskRepository.save(task);
        }
    }

    @Override
    public ResponseEntity<?> deleteTask(Long id) {
        taskRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    private List<TaskHistory> createTaskHistory(Task task) {
        List<TaskHistory> taskHistories = new ArrayList<>();
        TaskHistory taskHistory = new TaskHistory();

        taskHistory.setTask(task);
        taskHistory.setStatus(task.getStatus());
        taskHistory.setDateTime(LocalDateTime.now());

        taskHistoryRepository.save(taskHistory);

        taskHistories.add(taskHistory);

        return taskHistories;
    }

    private TaskResponse createTaskResponse(Task task) {
        List<TaskHistoryResponse> historyResponses = createTaskHistoryResponseList(task.getTaskHistories());

        return new TaskResponse(task.getId(),
                task.getCustomer().getName(),
                task.getStatus().toString(),
                historyResponses);
    }

    private List<TaskResponse> createTaskResponseList(Collection<Task> tasks) {
        return tasks.stream()
                .map(this::createTaskResponse)
                .collect(Collectors.toList());
    }

    private TaskHistoryResponse createTaskHistoryResponse(TaskHistory taskHistory) {
        return new TaskHistoryResponse(
                taskHistory.getStatus().toString(),
                taskHistory.getDateTime());
    }

    private List<TaskHistoryResponse> createTaskHistoryResponseList(Collection<TaskHistory> taskHistories) {
        return taskHistories.stream()
                .map(this::createTaskHistoryResponse)
                .collect(Collectors.toList());
    }

    private TaskMessage createTaskMessage(Task task) {
        return new TaskMessage(task.getName(),
                task.getStatus().toString());
    }


    // TEST RESPONSE RABBITMQ
    private void receiveTaskMessage(TaskMessage taskMessage) {
        Thread thread = new Thread(() -> {
            TaskMessage task = new TaskMessage(
                    taskMessage.getName(),
                    "RENDERING"
            );

            log.info("send task response render " + LocalDateTime.now());
            rabbitTemplate.convertAndSend("render.task.response", task);

            long randomTime = (long) ((Math.random() * (300_000 - 60_000)) + 60_000);

            try {
                Thread.sleep(randomTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            task.setStatus("COMPLETE");

            log.info("send task response complete " + LocalDateTime.now());
            rabbitTemplate.convertAndSend("render.task.response", task);
        });

        thread.start();
    }
}

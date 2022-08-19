package com.render.server.listener;

import com.render.server.domain.Task;
import com.render.server.domain.enums.Status;
import com.render.server.dto.message.TaskMessage;
import com.render.server.repository.TaskRepository;
import com.render.server.service.TaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskListener {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskListener(TaskService taskService,
                        TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @RabbitListener(queues = "${renderf.messaging.task.response}")
    public void receiveOrder(TaskMessage taskMessage) {
        Optional<Task> taskOptional = taskRepository.findByName(taskMessage.getName());

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            taskService.addHistoryTaskInTask(task.getId(), Status.valueOf(taskMessage.getStatus()));

            task.setStatus(Status.valueOf(taskMessage.getStatus()));

            taskRepository.save(task);
        }
    }
}

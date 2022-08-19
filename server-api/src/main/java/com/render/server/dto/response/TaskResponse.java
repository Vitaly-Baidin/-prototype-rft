package com.render.server.dto.response;

import com.render.server.domain.TaskHistory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String customerName;

    private String status;

    private List<TaskHistoryResponse> taskHistory;
}

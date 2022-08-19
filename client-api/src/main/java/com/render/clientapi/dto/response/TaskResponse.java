package com.render.clientapi.dto.response;

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

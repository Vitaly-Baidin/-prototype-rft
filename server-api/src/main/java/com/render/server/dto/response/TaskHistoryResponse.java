package com.render.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskHistoryResponse {

    private String status;

    private LocalDateTime dateTime;
}

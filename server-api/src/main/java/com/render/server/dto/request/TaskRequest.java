package com.render.server.dto.request;

import lombok.Data;

@Data
public class TaskRequest {

    private String name;

    private String customerName;

    private String status;

}

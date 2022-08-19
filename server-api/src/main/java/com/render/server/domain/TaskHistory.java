package com.render.server.domain;

import com.render.server.domain.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class TaskHistory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    private LocalDateTime dateTime;
}

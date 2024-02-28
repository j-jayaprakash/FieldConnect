package com.jp.field_connect.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class WorkHistoryDTO  implements Serializable{
    private String workName;
    private String salPerHour;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private WorkerDetailsDTO workerID;
}


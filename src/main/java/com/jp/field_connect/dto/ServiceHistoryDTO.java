package com.jp.field_connect.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceHistoryDTO implements Serializable {
    private FormerDetailsDTO former;
    private WorkerDetailsDTO worker;
    private ServiceDetailsDTO service;
    private LocalDateTime bookingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bookingStatus;
    private String additionalDetails;
    private BigDecimal paymentAmount;
}


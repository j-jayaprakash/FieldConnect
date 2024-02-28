package com.jp.field_connect.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class ServiceHistory {

	@Id
	@GenericGenerator(name = "inc", strategy = "increment")
	@GeneratedValue(generator = "inc")
	private Long serviceHistoryID;

    @ManyToOne
    @JoinColumn(name = "formerID")
    private FormerDetails former;

    @ManyToOne
    @JoinColumn(name = "workerID")
    private WorkerDetails worker;

    @ManyToOne
    @JoinColumn(name = "serviceID")
    private ServiceDetails service;

    private LocalDateTime bookingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bookingStatus;
    private String additionalDetails;
    private BigDecimal paymentAmount;

}

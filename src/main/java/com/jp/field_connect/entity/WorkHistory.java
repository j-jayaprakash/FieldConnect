package com.jp.field_connect.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class WorkHistory {

	@Id
	@GenericGenerator(name = "inc", strategy = "increment")
	@GeneratedValue(generator = "inc")
	private Long workId;

	private String workName;
	private String salPerHour;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	@ManyToOne
	@JoinColumn(name = "formerID")
	private FormerDetails former;

	private LocalDateTime bookingDate;

	private String bookingStatus;
	private String additionalDetails;
	private BigDecimal paymentAmount;

	@ManyToOne
	@JoinColumn(name = "workerID")
	private WorkerDetails workerID;

}

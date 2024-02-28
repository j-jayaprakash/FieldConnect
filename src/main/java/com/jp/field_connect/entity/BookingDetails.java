package com.jp.field_connect.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetails {


	@Id
	@GenericGenerator(name = "inc", strategy = "increment")
	@GeneratedValue(generator = "inc")
	private Long boookingID;

	
	private LocalDateTime startsOn;

	private LocalDateTime endsOn;
	
	private String type;
	
	private Double ServiceCost;

	private Boolean bookingStatus;
	
	@Column( updatable = false)
	@CreationTimestamp
	private LocalDateTime bookedDate;
	@Column(name = "updated_date", insertable = false)
	@UpdateTimestamp
	private LocalDateTime updatedDateTime;
	
    @ManyToOne
    @JoinColumn(name = "formerID")
    private FormerDetails former;

    @ManyToOne
    @JoinColumn(name = "workerID")
    private WorkerDetails worker;

    @ManyToOne
    @JoinColumn(name = "serviceID")
    private ServiceDetails service;


	
}

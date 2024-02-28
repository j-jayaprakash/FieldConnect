package com.jp.field_connect.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetails {

    @Id
    @GenericGenerator(name = "inc", strategy = "increment")
    @GeneratedValue(generator = "inc")
    private Long workerId;
    
	private String availablityStatus;
	@Embedded
	private PersonalInfo personalInfo;
	@Embedded
	private Address address;

	@Column(name = "created_date", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdDateTime;
	@Column(name = "updated_date", insertable = false)
	@UpdateTimestamp
	private LocalDateTime updatedDateTime;
	
	@JsonIgnore
    @OneToOne
    @JoinColumn(name = "userID")
    private Users user;


	@OneToMany
	private List<WorkHistory> workList;

}

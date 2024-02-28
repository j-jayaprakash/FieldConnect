package com.jp.field_connect.entity;

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

import lombok.Data;

@Data
@Entity
public class ServiceDetails {
	@Id
	@GenericGenerator(name = "inc", strategy = "increment")
	@GeneratedValue(generator = "inc")
	private Long serviceId;
	private String companyName;
	private String serviceName;
	private String costPerHour;

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private ServiceProviderDetails serviceProvider;

	@Column(name = "created_date", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdDateTime;
	@Column(name = "updated_date", insertable = false)
	@UpdateTimestamp
	private LocalDateTime updatedDateTime;
	


}

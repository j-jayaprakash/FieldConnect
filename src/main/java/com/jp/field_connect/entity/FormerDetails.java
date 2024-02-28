package com.jp.field_connect.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormerDetails {
	
	 @Id
	    @GenericGenerator(name = "inc", strategy = "increment")
	    @GeneratedValue(generator = "inc")
	    private Long FormerId;
	    
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

	    
	    

}

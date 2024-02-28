package com.jp.field_connect.entity;

import javax.persistence.Column;

import lombok.Data;

@Data
public class PersonalInfo {

	@Column(name="email_id")
	private String emailId;
	@Column(name="gender")
	private String gender;
	@Column(name="age")
	private Integer age;
    @Column(length = 500)
    private String profileImageUrl;
    @Column(name = "mobile", nullable = false)
    private String mobile;
	
}

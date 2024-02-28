package com.jp.field_connect.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PersonalInfoDTO implements Serializable {
	private String emailId;
	private String gender;
	private Integer age;
    private String mobile;
}

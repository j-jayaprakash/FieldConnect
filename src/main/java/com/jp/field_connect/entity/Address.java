package com.jp.field_connect.entity;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Address {

	private String doorNo;
	private String addrLine1;
	private String addrLine2;
	private String city;
	private String district;
	private String state;
	private String country; 
}

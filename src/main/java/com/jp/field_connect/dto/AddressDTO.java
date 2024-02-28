package com.jp.field_connect.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AddressDTO implements Serializable {

	
	private String doorNo;
	private String addrLine1;
	private String addrLine2;
	private String city;
	private String district;
	private String state;
	private String country; 
}

package com.jp.field_connect.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppResponseDTO implements Serializable{

	private String status;
	private int statusCode;
	private Object data;
	private String error;
}

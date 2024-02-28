package com.jp.field_connect.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkerDetailsDTO implements Serializable {
   
	private Long workerId;
    private PersonalInfoDTO personalInfo;
    private AddressDTO address;

}


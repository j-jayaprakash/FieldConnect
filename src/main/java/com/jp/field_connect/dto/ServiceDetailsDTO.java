package com.jp.field_connect.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ServiceDetailsDTO implements Serializable {

    private String companyName;
    private String serviceName;
    private String costPerHour;
}


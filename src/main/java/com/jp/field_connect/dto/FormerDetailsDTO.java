package com.jp.field_connect.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FormerDetailsDTO implements Serializable {

    private PersonalInfoDTO personalInfo;
    private AddressDTO address;

}


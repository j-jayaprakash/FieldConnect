package com.jp.field_connect.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UsersDTO implements Serializable {
    private String userName;
    private boolean enabled;
    private String password;
    private String roles;
    private PersonalInfoDTO personalInfoDTO;
    private AddressDTO addressDTO;
}




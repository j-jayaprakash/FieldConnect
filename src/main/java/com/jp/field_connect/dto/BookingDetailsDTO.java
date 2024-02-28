package com.jp.field_connect.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BookingDetailsDTO implements Serializable{

    private LocalDateTime startsOn;
    private LocalDateTime endsOn;
    private String type;
    private Double serviceCost;
    private Boolean bookingStatus;
    
    private List<Long> workerIds;
    private Long serviceId;
}


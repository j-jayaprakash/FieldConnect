package com.jp.field_connect.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.field_connect.entity.BookingDetails;

public interface BookingDetailsRepo extends JpaRepository<BookingDetails,Long> {

}

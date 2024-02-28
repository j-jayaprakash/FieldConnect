package com.jp.field_connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp.field_connect.entity.ServiceDetails;

@Repository
public interface ServiceRepo extends JpaRepository<ServiceDetails, Long> {

}

package com.jp.field_connect.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.field_connect.entity.ServiceProviderDetails;

public interface ServiceProviderRepo extends JpaRepository<ServiceProviderDetails, Long> {

}

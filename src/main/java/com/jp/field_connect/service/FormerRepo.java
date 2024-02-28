package com.jp.field_connect.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.field_connect.entity.FormerDetails;

public interface FormerRepo extends JpaRepository<FormerDetails, Long> {

}

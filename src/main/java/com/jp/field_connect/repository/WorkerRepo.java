package com.jp.field_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jp.field_connect.entity.WorkerDetails;
@Repository
public interface WorkerRepo extends JpaRepository<WorkerDetails, Long> {

	
	@Modifying
	@Query("UPDATE WorkerDetails wd set wd.availablityStatus=:status WHERE workerId IN :ids")
	int updateStatusById(@Param("status") String status,@Param("ids") List<Long> ids);

}

package com.jp.field_connect.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jp.field_connect.entity.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {


	public Users findByUserName(String userName);
	public List<Users> findByRoles(String  role);

	public boolean existsByUserName(String userName);
}

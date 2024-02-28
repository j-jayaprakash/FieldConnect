package com.jp.field_connect.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jp.field_connect.repository.UsersRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsersRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		com.jp.field_connect.entity.Users userDetails = userRepo.findByUserName(username);
		if (userDetails == null)
			throw new UsernameNotFoundException(" User " + username + "not found!");

		 List<SimpleGrantedAuthority> authority = userDetails.getRoles().stream().map((role) -> {

			return new SimpleGrantedAuthority(role);
		}).collect(Collectors.toList());

		return new User(username, userDetails.getPassword(), authority);
	}

}

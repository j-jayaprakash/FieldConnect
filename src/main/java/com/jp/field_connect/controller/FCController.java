package com.jp.field_connect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jp.field_connect.constants.AppConstants;
import com.jp.field_connect.dto.AppResponseDTO;
import com.jp.field_connect.dto.BookingDetailsDTO;
import com.jp.field_connect.dto.ServiceDetailsDTO;
import com.jp.field_connect.dto.UsersDTO;
import com.jp.field_connect.service.FCService;

@RestController
public class FCController {

	@Autowired
	private FCService userService;

	@GetMapping("/")
	public AppResponseDTO getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		AppResponseDTO login = userService.assignUser(name);
		return login;
	}

//	@GetMapping("/resendOtp")
//	public AppResponseDto resendOtp() {
//		return authService.resendOtp();
//	}

	@PostMapping(value = AppConstants.REGISTER_USER_URL)
	public AppResponseDTO registerUser(@RequestBody UsersDTO userDto, @PathVariable("role") String role) {

		AppResponseDTO registerUser = userService.registerUser(userDto, role);

		return registerUser;
	}

//	@PostMapping("/validate/{otp}")
//	private AppResponseDto activateAccount(@PathVariable(value = "otp") String otp) {
//		return authService.activateUser(otp);
//
//	}

	@PostMapping(value = AppConstants.UPDATE_PROFILE_IMAGE_URL)
	public AppResponseDTO updateProfileImage(@RequestPart(value = "file") MultipartFile file) {

		return userService.updateProfileImage(file);

	}

	@PutMapping("/change/{status}")
	public AppResponseDTO updateAvailablity(@PathVariable("status") String status) {
		AppResponseDTO changeAvailablity = userService.changeAvailablity(status);

		return changeAvailablity;
	}

	@GetMapping("/workerlist")
	public AppResponseDTO getAllWorkers() {

		AppResponseDTO gellAllWorkers = userService.gellAllWorkers();
		return gellAllWorkers;
	}

	@PostMapping("/bookWorkerOrService")
	public AppResponseDTO bookWorkers(@RequestBody BookingDetailsDTO bookDto) {

		AppResponseDTO response = userService.bookWorkersAndService(bookDto);
		return response;
	}

	@PostMapping("/addService")
	public AppResponseDTO registerService(@RequestBody ServiceDetailsDTO serviceDto) {

		AppResponseDTO registerService = userService.registerService(serviceDto);
		return registerService;
	}
	
	@GetMapping("servicelist")
	public AppResponseDTO getAllServices() {
		return userService.getAllServices();
	}

}

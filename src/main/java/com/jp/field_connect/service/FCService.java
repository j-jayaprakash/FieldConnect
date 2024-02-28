package com.jp.field_connect.service;

import org.springframework.web.multipart.MultipartFile;

import com.jp.field_connect.dto.AppResponseDTO;
import com.jp.field_connect.dto.BookingDetailsDTO;
import com.jp.field_connect.dto.ServiceDetailsDTO;
import com.jp.field_connect.dto.UsersDTO;

public interface FCService {

	public AppResponseDTO registerUser(UsersDTO userDto, String role);
	
//	public AppResponseDto activateUser(String Otp);
//	AppResponseDTO resendOtp();

	public AppResponseDTO assignUser(String name);

	AppResponseDTO updateProfileImage(MultipartFile file);

	public String uploadFile(MultipartFile mFile);

	AppResponseDTO changeAvailablity(String status);

	AppResponseDTO gellAllWorkers();

	public AppResponseDTO registerService(ServiceDetailsDTO serviceDto);

	AppResponseDTO bookWorkersAndService(BookingDetailsDTO bookingDTO);

	AppResponseDTO getAllServices();

}

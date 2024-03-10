package com.jp.field_connect.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jp.field_connect.constants.AppConstants;
import com.jp.field_connect.dto.AppResponseDTO;
import com.jp.field_connect.dto.BookingDetailsDTO;
import com.jp.field_connect.dto.ServiceDetailsDTO;
import com.jp.field_connect.dto.UsersDTO;
import com.jp.field_connect.entity.Address;
import com.jp.field_connect.entity.BookingDetails;
import com.jp.field_connect.entity.FormerDetails;
import com.jp.field_connect.entity.PersonalInfo;
import com.jp.field_connect.entity.ServiceDetails;
import com.jp.field_connect.entity.ServiceProviderDetails;
import com.jp.field_connect.entity.Users;
import com.jp.field_connect.entity.WorkerDetails;
import com.jp.field_connect.repository.BookingDetailsRepo;
import com.jp.field_connect.repository.FormerRepo;
import com.jp.field_connect.repository.ServiceProviderRepo;
import com.jp.field_connect.repository.ServiceRepo;
import com.jp.field_connect.repository.UsersRepo;
import com.jp.field_connect.repository.WorkerRepo;
import com.jp.field_connect.repository.WorkerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FCServiceImpl implements FCService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private ServiceRepo serviceRepo;

	private Users currentUser;

	@Autowired
	private UsersRepo usersRepo;

	@Autowired
	private FormerRepo formerRepo;

	@Autowired
	private ServiceProviderRepo serviceProviderRepo;
	@Autowired
	private WorkerRepository workerDetailsRepo;
	@Autowired
	private BookingDetailsRepo bookingRepo;

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private WorkerRepo workerRepo;

	@Value("${amazon.aws.s3.bucket-name}")
	private String bucketName;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public AppResponseDTO registerUser(UsersDTO userDto, String role) {

		try {

			log.info("register user method startd");

			// Check if the username is already taken
			if (usersRepo.existsByUserName(userDto.getUserName())) {
				throw new RuntimeException("Username already exists");
			}

			// Hash the password securely
			String encodedPassword = encoder.encode(userDto.getPassword());

			// Create a new user entity
			Users user = new Users();
			user.setUserName(userDto.getUserName());
			user.setPassword(encodedPassword);
			user.setEnabled(userDto.isEnabled());
			HashSet<String> hashSet = new HashSet<String>();
			hashSet.add(userDto.getRoles());
			user.setRoles(hashSet);
			// Save the user to the database
			Users savedUser = usersRepo.save(user);

			// Determine the role and save role-specific details
			PersonalInfo personalInfo = new PersonalInfo();
			Address address = new Address();
			BeanUtils.copyProperties(userDto.getAddressDTO(), address);
			BeanUtils.copyProperties(userDto.getPersonalInfoDTO(), personalInfo);

			Object currUser = null;

			switch (role) {
			case "FORMER":

				FormerDetails formerDetails = new FormerDetails();

				formerDetails.setAddress(address);
				formerDetails.setPersonalInfo(personalInfo);
				formerDetails.setUser(savedUser);
				currUser = formerRepo.save(formerDetails);

				log.info("Former roled user saved");
				break;
			case "SERVICE_PROVIDER":
				ServiceProviderDetails serviceProvider = new ServiceProviderDetails();
				serviceProvider.setAddress(address);
				serviceProvider.setPersonalInfo(personalInfo);
				serviceProvider.setUser(savedUser);
				currUser = serviceProviderRepo.save(serviceProvider);
				log.info("Service provider roled user saved");
				break;
			case "WORKER":
				WorkerDetails worker = new WorkerDetails();
				worker.setAddress(address);
				worker.setPersonalInfo(personalInfo);
				worker.setUser(savedUser);
				worker.setAvailablityStatus("AVAILABLE");
				currUser = workerDetailsRepo.save(worker);
				log.info("Worker roled user saved");
				break;
			default:
				log.error("Invalid User Role " + role);
				throw new IllegalArgumentException("Invalid role");
			}

			if (currUser instanceof FormerDetails)
				savedUser.setFormerDetails((FormerDetails) currUser);
			else if (currUser instanceof WorkerDetails)
				savedUser.setWorkerDetails((WorkerDetails) currUser);
			else
				savedUser.setServiceProviderDetails((ServiceProviderDetails) currUser);

			currUser = usersRepo.save(savedUser);

			log.info("registration successful");
			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS, user,
					null);
		} catch (Exception e) {

			log.error("Exception occured " + e.getLocalizedMessage());
			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_INTERNAL_SERVER_ERROR,
					AppConstants.STATUS_CODE_INTERNAL_SERVER_ERROR, null, e.getLocalizedMessage());
		}
	}

	@Override
	public String uploadFile(MultipartFile mFile) {

		File fileObj = convertMultiToSingle(mFile);

		String fileName = System.currentTimeMillis() + "_" + mFile.getOriginalFilename();

		PutObjectRequest putReq = new PutObjectRequest(bucketName, fileName, fileObj);
		s3Client.putObject(putReq);

		Date expiration = new Date(System.currentTimeMillis() + 3600000*24*7); // 1 hour from now
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
				.withExpiration(expiration);
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

		fileObj.delete();

		return url.toString();
	}

	@Override
	public AppResponseDTO updateProfileImage(MultipartFile file) {
		String profileUrl = uploadFile(file);
		if (currentUser == null)
			assignUser(SecurityContextHolder.getContext().getAuthentication().getName());

		currentUser.getRoles().forEach((role) -> {
			if (role.equals(AppConstants.ROLE_FORMER))
				currentUser.getFormerDetails().getPersonalInfo().setProfileImageUrl(profileUrl);
			else if (role.equals(AppConstants.ROLE_SERVICE_PROVIDER))
				currentUser.getServiceProviderDetails().getPersonalInfo().setProfileImageUrl(profileUrl);
			else
				currentUser.getWorkerDetails().getPersonalInfo().setProfileImageUrl(profileUrl);

		});

		usersRepo.save(currentUser);
		return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS,
				"user Profile photo added", null);
	}

	private File convertMultiToSingle(MultipartFile mFile) {

		File convertedFile = new File(mFile.getOriginalFilename());

		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {

			fos.write(mFile.getBytes());

		} catch (IOException e) {

			e.printStackTrace();
		}
		return convertedFile;
	}

	@Override
	public AppResponseDTO changeAvailablity(String status) {

		if (currentUser == null)
			assignUser(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!currentUser.getWorkerDetails().getAvailablityStatus().equals(status)) {
			currentUser.getWorkerDetails().setAvailablityStatus(status);
			usersRepo.save(currentUser);
		}

		return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS,
				"status Changed successful", null);

	}

	@Override
	public AppResponseDTO gellAllWorkers() {
		try {
			List<WorkerDetails> findByRole = workerRepo.findAll();
			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS, findByRole,
					null);
		} catch (Exception e) {

			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_INTERNAL_SERVER_ERROR,
					AppConstants.STATUS_CODE_INTERNAL_SERVER_ERROR, null, e.getLocalizedMessage());
		}

	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public AppResponseDTO bookWorkersAndService(BookingDetailsDTO bookingDTO) {
		
		if (currentUser == null)
			assignUser(SecurityContextHolder.getContext().getAuthentication().getName());

		HashSet<BookingDetails> bookinglist = new HashSet<BookingDetails>();
		if (bookingDTO.getType().equals(AppConstants.BOOKING_TYPE_WORKER)) {

			bookingDTO.getWorkerIds().forEach((workerId) -> {

				WorkerDetails worker = workerDetailsRepo.findById(workerId)
						.orElseThrow(() -> new IllegalArgumentException("Worker not found"));

				if (!worker.getAvailablityStatus().equals("BOOKED")) {
					// Create a new booking for worker
					BookingDetails bookingDetails = new BookingDetails();
					bookingDetails.setWorker(worker);
					bookingDetails.setStartsOn(bookingDTO.getStartsOn());
					bookingDetails.setEndsOn(bookingDTO.getEndsOn());
					bookingDetails.setServiceCost(bookingDTO.getServiceCost());
					bookingDetails.setBookingStatus(true);
					bookingDetails.setType(bookingDTO.getType());
					bookingDetails.setFormer(currentUser.getFormerDetails());

					bookinglist.add(bookingDetails);

				}
			});
		} else if (bookingDTO.getType().equals(AppConstants.BOOKING_TYPE_SERVICE)) {

			Long serviceId = bookingDTO.getServiceId();
			ServiceDetails service = serviceRepo.findById(serviceId)
					.orElseThrow(() -> new IllegalArgumentException("Service not found"));

			// Create a new booking for service
			BookingDetails bookingDetails = new BookingDetails();
			bookingDetails.setService(service);
			bookingDetails.setStartsOn(bookingDTO.getStartsOn());
			bookingDetails.setEndsOn(bookingDTO.getEndsOn());
			bookingDetails.setServiceCost(bookingDTO.getServiceCost());
			bookingDetails.setBookingStatus(true); // Assuming booking is confirmed by default
			bookingDetails.setType(bookingDTO.getType());
			bookingDetails.setFormer(currentUser.getFormerDetails());

			bookinglist.add(bookingDetails);

		}
		;

		try {
			List<BookingDetails> saveAll = bookingRepo.saveAll(bookinglist);
			workerRepo.updateStatusById("BOOKED", bookingDTO.getWorkerIds());

			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS, saveAll,
					null);
		} catch (Exception e) {
			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_INTERNAL_SERVER_ERROR,
					AppConstants.STATUS_CODE_INTERNAL_SERVER_ERROR, null, e.getLocalizedMessage());

		}

	}

	@Override
	public AppResponseDTO assignUser(String name) {

		try {

			Users userDetails = usersRepo.findByUserName(name);
			currentUser = userDetails;
			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS,
					currentUser, null);

		} catch (Exception e) {
			return new AppResponseDTO(AppConstants.STATUS_MESSAGE_INTERNAL_SERVER_ERROR,
					AppConstants.STATUS_CODE_INTERNAL_SERVER_ERROR, null, e.getLocalizedMessage());
		}
	}

	@Override
	public AppResponseDTO registerService(ServiceDetailsDTO serviceDto) {
		
		if (currentUser == null)
			assignUser(SecurityContextHolder.getContext().getAuthentication().getName());

		ServiceDetails serviceDetails = new ServiceDetails();

		BeanUtils.copyProperties(serviceDto, serviceDetails);
		serviceDetails.setServiceProvider(currentUser.getServiceProviderDetails());

		ServiceDetails save = serviceRepo.save(serviceDetails);
		return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS, save, null);
	}

	@Override
	public AppResponseDTO getAllServices() {

		List<ServiceDetails> serviceList = serviceRepo.findAll();

		return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS, serviceList,
				null);
	}

//	Supplier<String> otpGen = () -> {
//
//		int length = 6;
//		String otp = "";
//		for (int i = 0; i < length; i++)
//			otp += (int) (Math.random() * 10);
//		return otp;
//
//	};

//@Override
//public AppResponseDTO resendOtp() {
//
//	currentUser.setSecretKey(sendOtp(currentUser.getPersonalInfo().getEmailId()));
//	userRepo.save(currentUser);
//	return new AppResponseDTO(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS,
//			"otp sent successfull", null);
//
//}

//	private String sendOtp(String email) {
//
//		try {
//
//			SimpleMailMessage message = new SimpleMailMessage();
//			message.setTo(email);
//			message.setSubject("Otp for your FieldConnect Registration");
//			String otp = otpGen.get();
//			message.setText(otp);
//			sender.send(message);
//			return otp;
//		} catch (Exception e) {
//			return null;
//		}
//	}

//@Override
//public AppResponseDto activateUser(String newOtp) {
//
//	if (newOtp.equals(currentUser.getSecretKey())) {
//
//		if (!currentUser.isEnabled()) {
//			currentUser.setEnabled(true);
//			UserDetails save = repo.save(currentUser);
//			currentUser = save;
//			return new AppResponseDto(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS,
//					"user Activated", null);
//		}
//		return new AppResponseDto(AppConstants.STATUS_MESSAGE_SUCCESS, AppConstants.STATUS_CODE_SUCCESS,
//				"user Already Activated", null);
//
//	} else
//		return new AppResponseDto(AppConstants.STATUS_MESSAGE_NOT_FOUND, AppConstants.STATUS_CODE_NOT_FOUND, null,
//				"invalid Otp");
//
//}

}
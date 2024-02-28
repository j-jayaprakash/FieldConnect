package com.jp.field_connect.constants;

public interface AppConstants {
	
	
	String ROLE_WORKER="WORKER";
	String ROLE_SERVICE_PROVIDER="SERVICE_PROVIDER";
	String ROLE_FORMER="FORMER";
	
	String BOOKING_TYPE_SERVICE="SERVICE";
	String BOOKING_TYPE_WORKER="WORKER";
	
	
	
    String AUTH_BASE_URL = "/auth";
    String LOGIN_URL =  "/login";
    String GET_CURRENT_USER_URL =  "/get_user";
    String LOGOUT_URL =  "/logout";
    String UPDATE_PROFILE_IMAGE_URL =  "/addimage";
    String REGISTER_USER_URL =  "/register/{role}";
	
    String WORKERS_BASE_URL = "/workers";
    
    String FORMER_BASE_URL = "/former";
    String BOOK_FIELD_WORKER_URL =  "/{formerId}/book/fieldworker/{fieldWorkerId}";
    String BOOK_SERVICE_URL =  "/{formerId}/book/service/{serviceId}";

    String PROVIDER_BASE_URL="/provider";
    
    
    int STATUS_CODE_SUCCESS = 200;
    int STATUS_CODE_BAD_REQUEST = 400;
    int STATUS_CODE_UNAUTHORIZED = 401;
    int STATUS_CODE_NOT_FOUND = 404;
    int STATUS_CODE_INTERNAL_SERVER_ERROR = 500;

    String STATUS_MESSAGE_SUCCESS = "Success";
    String STATUS_MESSAGE_BAD_REQUEST = "Bad Request";
    String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    String STATUS_MESSAGE_NOT_FOUND = "Not Found";
    String STATUS_MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error";
}

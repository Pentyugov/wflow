package com.pentyugov.wflow.application.configuration.constant;

public class ApplicationConstants {

    public interface Security {
        long EXPIRATION_TIME = 432_000_00;  // 5 days in milliseconds
        String TOKEN_PREFIX = "Bearer ";
        String JWT_TOKEN_HEADER = "Jwt-Token";
        String AUTHORIZATION = "Authorization";
        String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
        String GET_ARRAYS_LLC = "Get arrays, LLC";
        String GET_ARRAYS_ADMINISTRATION = "Organization management portal";
        String AUTHORITIES = "authorities";
        String FORBIDDEN_MESSAGE = "You need to login to access this page";
        String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
        String OPTIONS_HTTP_METHODS = "OPTIONS";
        String [] PUBLIC_URLS = {"/user/login",
                "/api/user/register",
                "/api/user/reset-password/**",
                "/api/user/image/**",
                "/api/auth/**",
                "/test/**",
                "/ws/**"};
//        public static final String [] PUBLIC_URLS = {"**"};
    }

    public interface File {
        String API_PREFIX = "/api";
        String USER_IMAGE_PATH = "/user/image/";
        String JPG_EXTENSION = "jpg";
        String USER_FOLDER = System.getProperty("user.home") + "/wflow/user/";
        String REDIS_FOLDER = System.getProperty("user.home") + "/wflow/data/redis";
        String DIRECTORY_CREATED = "Created directory for: ";
        String DEFAULT_USER_IMAGE_PATH = "api/user/image/";
        String FILE_SAVED_IN_FILE_SYSTEM = "Saved file in file system by name: ";
        String DOT = ".";
        String FORWARD_SLASH = "/";
        String NOT_AN_IMAGE_FILE = " is not an image file. Please upload an image file";
        String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";
        String PROFILE_IMAGE_DELETE_API = "https://api.uploadcare.com/files/";
        String PROFILE_IMAGE_RESOURCE_HOST = "https://ucarecdn.com/";

    }

    public interface Email {
        String SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtps";
        String USERNAME = "infojuniorro@gmail.com";
        String PASSWORD = "N99gmail1@";
        String FROM_EMAIL = "support@getarrays.com";
        String CC_EMAIL = "";
        String EMAIL_SUBJECT = "Get Arrays, LLC - New Password";
        String GMAIL_SMTP_SERVER = "mail.zolloz.site";
        String SMTP_HOST = "mail.smtp.host";
        String SMTP_AUTH = "mail.smtp.auth";
        String SMTP_PORT = "mail.smtp.port";
        int DEFAULT_PORT = 465;
        String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
        String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    }

    public interface Websocket {
        String NOTIFICATION_DESTINATION = "/notifications";
        String CHAT_MESSAGES_DESTINATION = "/chat/messages";
        String NEW_MESSAGES_COUNT_DESTINATION = "/chat/new-messages-count";
    }

    public interface Screen {
        String DEPARTMENT = "screen$Department";
        String EMPLOYEE = "screen$Employee";
        String POSITION = "screen$Position";
        String USER = "screen$User";
        String PROJECT = "screen$Project";
        String TICKET = "screen$Ticket";
        String PROFILE = "screen$Profile";


        interface Action {
            String SCREEN_ACTION_BROWSE = "Browse";
            String SCREEN_ACTION_CREATE = "Create";
            String SCREEN_ACTION_EDIT = "Edit";
            String SCREEN_ACTION_DELETE = "Delete";
        }

    }


}

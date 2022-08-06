package com.pentyugov.wflow.application.configuration.constant;

public class ApplicationConstants {

    public interface Security {
        long EXPIRATION_TIME = 432_000_00;  // 5 days in milliseconds
        String TOKEN_PREFIX = "Bearer ";
        String JWT_TOKEN_HEADER = "Jwt-Token";
        String AUTHORIZATION = "Authorization";
        String GET_ARRAYS_LLC = "Get arrays, LLC";
        String GET_ARRAYS_ADMINISTRATION = "Organization management portal";
        String AUTHORITIES = "authorities";
        String FORBIDDEN_MESSAGE = "You need to login to access this page";
        String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
        String[] PUBLIC_URLS = {
            "/user/login",
            "/api/user/register",
            "/api/user/reset-password/**",
            "/api/user/image/**",
            "/api/auth/**",
            "/test/**",
            "/v3/**",
            "/ws/**"
        };
//        public static final String [] PUBLIC_URLS = {"**"};
    }

    public interface File {
        String REDIS_FOLDER = System.getProperty("user.home") + "/wflow/data/redis";
        String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";
        String PROFILE_IMAGE_DELETE_API = "https://api.uploadcare.com/files/";
        String PROFILE_IMAGE_RESOURCE_HOST = "https://ucarecdn.com/";

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

    public interface TelBot {
        String API_URL = "http://localhost:8090/api/v1";
        String TASK_CONTROLLER = API_URL + "/tasks";
        String TASKS_SEND_MESSAGE_ENDPOINT = TASK_CONTROLLER + "/send";

    }


}

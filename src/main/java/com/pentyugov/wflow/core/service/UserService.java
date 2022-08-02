package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.dto.UserDto;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.payload.request.SignUpRequest;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {

    String NAME = "wflow$UserService";

    User createUser(SignUpRequest userIn) throws UsernameExistException, EmailExistException, UsernameIsEmptyException, EmailIsEmptyException;

    void addNewUser(UserDto userDto, String profileImage) throws UsernameExistException, EmailExistException, UsernameIsEmptyException, EmailIsEmptyException;

    void updateLastLoginDate(User user);

    User updateUser(UserDto userDto) throws UsernameExistException, EmailExistException, UserNotFoundException, UsernameIsEmptyException, EmailIsEmptyException;

    void updateUser(User user);

    void deleteUser(UUID id);

    void resetPassword(String email) throws UserNotFoundException;

    void changePassword(String email, String password, String confirmPassword, Principal principal) throws UserNotFoundException, ValidationException;

    User updateProfileImage(UUID id, String profileImageUrl) throws UserNotFoundException;

    User getCurrentUser(Principal principal) throws UserNotFoundException;

    User getUserById(UUID id) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    User getUserByUsername(String username) throws UserNotFoundException;

    List<User> getAllUsers();

    List<User> getAllWithRole(String roleName);

    List<User> getAllWithAnyRole(String roleName);

    List<User> getUsersWithEmployee();

    List<User> getUsersWithoutEmployee();

    User createUserFromDto(UserDto userDto);

    UserDto createUserDto(UUID id, String username, String email, String firstName, String lastName, List<RoleDto> roles, String isActive, String isNonLocked);

    UserDto createUserDtoFromUser(User user);

    User updateUserFromDto(User currentUser, UserDto userDto);

    User getUserByPrincipal(Principal principal) throws UserNotFoundException;

    boolean isUserAdmin(User user);

    boolean isUserInRole(User user, String roleName);

    User deleteUserProfileImage(UUID id);

    List<User> findAllLoggedInTelegram();

    User getUserByTelUserId(Long telUserId) throws UserNotFoundException;
}

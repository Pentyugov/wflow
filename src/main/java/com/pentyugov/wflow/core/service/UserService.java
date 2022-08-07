package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.dto.UserDto;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.payload.request.SignUpRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String NAME = "wflow$UserService";

    List<User> getAll();

    User getById(UUID id) throws UserNotFoundException;

    User add(SignUpRequest userIn) throws UsernameExistException, EmailExistException, UsernameIsEmptyException,
            EmailIsEmptyException;

    void add(UserDto userDto, String profileImage) throws UsernameExistException, EmailExistException, UsernameIsEmptyException,
            EmailIsEmptyException;

    void updateLastLoginDate(User user);

    User update(UserDto userDto) throws UsernameExistException, EmailExistException, UserNotFoundException, UsernameIsEmptyException,
            EmailIsEmptyException;

    void update(User user);

    User update(User currentUser, UserDto userDto);

    void delete(UUID id);

    User getCurrentUser();

    User getByEmail(String email) throws UserNotFoundException;

    User getByUsername(String username) throws UserNotFoundException;

    List<User> getAllWithRole(String roleName);

    List<User> getAllWithAnyRole(String roleName);

    List<User> getUsersWithEmployee();

    List<User> getUsersWithoutEmployee();

    User getByTelUserId(Long telUserId) throws UserNotFoundException;

    void resetPassword(String email) throws UserNotFoundException;

    void changePassword(String email, String password, String confirmPassword) throws ValidationException, UserNotFoundException;

    User updateProfileImage(UUID id, String profileImageUrl) throws UserNotFoundException;

    User convert(UserDto userDto);

    UserDto convert(UUID id, String username, String email, String firstName, String lastName, List<RoleDto> roles, String isActive,
                    String isNonLocked);

    UserDto convert(User user);

    User deleteUserProfileImage(UUID id);

    List<User> findAllLoggedInTelegram();


}

package com.pentyugov.wflow.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.dto.UserDto;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController extends AbstractController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<UserDto> userDtos = userService.getAllUsers()
                .stream()
                .map(userService::createUserDtoFromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) throws UserNotFoundException {
        UserDto userDto = userService.createUserDtoFromUser(userService.getUserById(UUID.fromString(id)));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<Object> getAllWithRole(@PathVariable String roleName) {
        List<UserDto> userDtos = userService.getAllWithRole(roleName)
                .stream()
                .map(userService::createUserDtoFromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/any-role")
    public ResponseEntity<Object> getAllWithAnyRole(@RequestParam String roleNames) {
        List<UserDto> userDtos = userService.getAllWithAnyRole(roleNames)
                .stream()
                .map(userService::createUserDtoFromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/without-employee")
    public ResponseEntity<Object> getUsersWithoutEmployee() {
        List<UserDto> userDtos = userService.getUsersWithoutEmployee()
                .stream()
                .map(userService::createUserDtoFromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestParam("username") String username,
                                      @RequestParam("email") @Email String email,
                                      @RequestParam("firstName") String firstName,
                                      @RequestParam("lastName") String lastName,
                                      @RequestParam(value = "roles", required = false) String roles,
                                      @RequestParam("active") String isActive,
                                      @RequestParam("nonLocked") String isNotLocked,
                                      @RequestParam(value = "profileImage", required = false) String profileImage)
            throws UsernameExistException, EmailExistException, IOException, UsernameIsEmptyException,
            EmailIsEmptyException {

        ObjectMapper mapper = new ObjectMapper();
        List<RoleDto> roleDtos = new ArrayList<>(Arrays.asList(mapper.readValue(roles, RoleDto[].class)));
        UserDto userDto = userService.createUserDto(null, username, email, firstName, lastName, roleDtos, isActive, isNotLocked);
        userService.addNewUser(userDto, profileImage);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody UserDto userDto) throws UsernameExistException,
            EmailExistException,
            UserNotFoundException,
            UsernameIsEmptyException,
            EmailIsEmptyException {
        return new ResponseEntity<>(userService.createUserDtoFromUser(userService.updateUser(userDto)), HttpStatus.OK);
    }

    @PostMapping("/profile-image")
    public ResponseEntity<Object> updateUserProfileImage(@RequestParam String id,
                                                         @RequestParam String profileImageUrl) throws UserNotFoundException {
        UserDto userDto = userService.createUserDtoFromUser(userService.updateProfileImage(UUID.fromString(id), profileImageUrl));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        userService.deleteUser(UUID.fromString(id));
        String message = String.format("User with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

    @DeleteMapping("/profile-image/{id}")
    public ResponseEntity<Object> deleteProfileImage(@PathVariable String id) {
        UserDto userDto = userService.createUserDtoFromUser(userService.deleteUserProfileImage(UUID.fromString(id)));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}

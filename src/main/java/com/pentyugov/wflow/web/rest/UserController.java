package com.pentyugov.wflow.web.rest;

import static com.pentyugov.wflow.application.configuration.SwaggerConfig.BEARER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.dto.UserDto;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.http.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Get all users", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getAll() {
        List<UserDto> userDtos = userService.getAll()
                .stream()
                .map(userService::convert)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getById(@PathVariable String id) throws UserNotFoundException {
        UserDto userDto = userService.convert(userService.getById(UUID.fromString(id)));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/role/{roleName}")
    @Operation(summary = "Get all users with role (roleName in path variable)", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getAllWithRole(@PathVariable String roleName) {
        List<UserDto> userDtos = userService.getAllWithRole(roleName)
                .stream()
                .map(userService::convert)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/any-role")
    @Operation(summary = "Get all users with any role in path param", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getAllWithAnyRole(@RequestParam String roleNames) {
        List<UserDto> userDtos = userService.getAllWithAnyRole(roleNames)
                .stream()
                .map(userService::convert)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/without-employee")
    @Operation(summary = "Get all users without linked employee", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getUsersWithoutEmployee() {
        List<UserDto> userDtos = userService.getUsersWithoutEmployee()
                .stream()
                .map(userService::convert)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Post new user", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> post(@RequestParam("username") String username,
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
        UserDto userDto = userService.convert(null, username, email, firstName, lastName, roleDtos, isActive, isNotLocked);
        userService.add(userDto, profileImage);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update existing user", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> update(@RequestBody UserDto userDto) throws UsernameExistException,
            EmailExistException,
            UserNotFoundException,
            UsernameIsEmptyException,
            EmailIsEmptyException {
        return new ResponseEntity<>(userService.convert(userService.update(userDto)), HttpStatus.OK);
    }

    @PostMapping("/profile-image")
    @Operation(summary = "Update user profile image", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> updateUserProfileImage(@RequestParam String id,
                                                         @RequestParam String profileImageUrl) throws UserNotFoundException {
        UserDto userDto = userService.convert(userService.updateProfileImage(UUID.fromString(id), profileImageUrl));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    @Operation(summary = "Delete user by ID", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        userService.delete(UUID.fromString(id));
        String message = String.format("User with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

    @DeleteMapping("/profile-image/{id}")
    @Operation(summary = "Delete user profile image", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> deleteProfileImage(@PathVariable String id) {
        UserDto userDto = userService.convert(userService.deleteUserProfileImage(UUID.fromString(id)));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}

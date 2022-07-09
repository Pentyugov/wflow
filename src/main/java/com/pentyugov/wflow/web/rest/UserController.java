package com.pentyugov.wflow.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.dto.UserDto;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.File.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController extends AbstractController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add-new-user")
    public ResponseEntity<Object> addNewUser(@RequestParam("username") String username,
                                             @RequestParam("email") @Email String email,
                                             @RequestParam("firstName") String firstName,
                                             @RequestParam("lastName") String lastName,
                                             @RequestParam(value = "roles", required = false) String roles,
                                             @RequestParam("isActive") String isActive,
                                             @RequestParam("isNotLocked") String isNotLocked,
                                             @RequestParam(value = "profileImage", required = false) String profileImage) throws UsernameExistException, EmailExistException, IOException, UsernameIsEmptyException, EmailIsEmptyException {
        ObjectMapper mapper = new ObjectMapper();
        List<RoleDto> roleDtos = new ArrayList<>(Arrays.asList(mapper.readValue(roles, RoleDto[].class)));
        UserDto userDto = userService.createUserDto(null, username, email, firstName, lastName, roleDtos, isActive, isNotLocked);
        userService.addNewUser(userDto, profileImage);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto) throws UsernameExistException,
                                                                     EmailExistException,
                                                                     UserNotFoundException,
                                                                     UsernameIsEmptyException,
                                                                     EmailIsEmptyException {
        return new ResponseEntity<>(userService.createUserDtoFromUser(userService.updateUser(userDto)), HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable String id) {
        userService.deleteUser(UUID.fromString(id));
        String message = String.format("User with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

    @GetMapping("/reset-password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable String email) throws UserNotFoundException {
        userService.resetPassword(email);
        String message = String.format("Email with a new password was sent to: %s", email);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/change-password")
    public ResponseEntity<HttpResponse> changePassword(@RequestParam(value = "password") String password,
                                                       @RequestParam(value = "confirmPassword") String confirmPassword,
                                                       Principal principal) throws UserNotFoundException, ValidationException {
        userService.changePassword(null, password, confirmPassword, principal);
        return response(HttpStatus.OK, "Password was successfully changed");
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<Object> getAllUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        userService.getAllUsers().forEach(user -> userDtos.add(userService.createUserDtoFromUser(user)));
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/get-all-with-role/{roleName}")
    public ResponseEntity<Object> getAllWithRole(@PathVariable String roleName) {
        List<UserDto> userDtos = userService.getAllWithRole(roleName)
                .stream()
                .map(userService::createUserDtoFromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/get-all-with-any-role")
    public ResponseEntity<Object> getAllWithAnyRole(@RequestParam String roleNames) {
        List<UserDto> userDtos = userService.getAllWithAnyRole(roleNames)
                .stream()
                .map(userService::createUserDtoFromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/get-all-with-permission/{permission}")
    public ResponseEntity<Object> getAllWithPermission(@PathVariable String permission) {
        List<UserDto> userDtos = userService.getAllWithPermission(permission)
                .stream()
                .map(userService::createUserDtoFromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/get-users-without-employee")
    public ResponseEntity<Object> getUsersWithoutEmployee() {
        List<UserDto> userDtos = userService.getUsersWithoutEmployee().stream().map(userService::createUserDtoFromUser).collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/get-by-username")
    public ResponseEntity<Object> getUserByUsername(@RequestParam String username) throws UserNotFoundException {
        UserDto userDto = userService.createUserDtoFromUser(userService.getUserByUsername(username));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<Object> getUserByEmail(@RequestParam String email) throws UserNotFoundException {
        UserDto userDto = userService.createUserDtoFromUser(userService.getUserByEmail(email));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable String id) throws UserNotFoundException {
        UserDto userDto = userService.createUserDtoFromUser(userService.getUserById(UUID.fromString(id)));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable String username, @PathVariable String filename) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename));
    }

    @GetMapping("/get-page")
    public ResponseEntity<Object> getPageOfUsers(@RequestParam Optional<Integer> pageNum,
                                                 @RequestParam Optional<Integer> size,
                                                 @RequestParam Optional<String> direction,
                                                 @RequestParam Optional<String> sortBy) {
        Sort.Direction dir = Sort.Direction.ASC;
        if (direction.isPresent() && "DESC".equals(direction.get().toUpperCase())) {
            dir = Sort.Direction.DESC;
        }
        Page<User> page = userService.getPageOfUsers(pageNum.orElse(0), size.orElse(5), dir, sortBy.orElse("id"));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(path = "/image/temp/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempImage( @PathVariable String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @PostMapping("/update-profile-image")
    public ResponseEntity<Object> updateUserProfileImage(@RequestParam String id,
                                                         @RequestParam String profileImageUrl) throws UserNotFoundException {
        UserDto userDto = userService.createUserDtoFromUser(userService.updateProfileImage(UUID.fromString(id), profileImageUrl));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete-profile-image/{id}")
    public ResponseEntity<Object> deleteUserProfileImage(@PathVariable String id) {
        UserDto userDto = userService.createUserDtoFromUser(userService.deleteUserProfileImage(UUID.fromString(id)));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

}

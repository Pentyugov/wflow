package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.application.configuration.security.jwt.JwtTokenProvider;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.UserDto;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.http.HttpResponse;
import com.pentyugov.wflow.web.payload.request.LoginRequest;
import com.pentyugov.wflow.web.payload.request.ResetPasswordRequest;
import com.pentyugov.wflow.web.payload.request.SignUpRequest;
import com.pentyugov.wflow.web.validator.ResponseErrorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Security;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends AbstractController {

    private final ResponseErrorValidator responseErrorValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult)
            throws UserNotFoundException {

        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = Security.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        UserDto userDto = userService.convert(userService.getByUsername(loginRequest.getUsername()));
        return new ResponseEntity<>(userDto, getJwtHeader(jwt), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult bindingResult)
            throws UsernameExistException, EmailExistException, UsernameIsEmptyException, EmailIsEmptyException {

        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        User user = userService.add(signUpRequest);
        UserDto userDto = userService.convert(user);
        return ResponseEntity.ok(userDto);

    }

    @PostMapping("/change-password")
    public ResponseEntity<HttpResponse> changePassword(@RequestParam(value = "email", required = false) String email,
                                                       @RequestParam(value = "password") String password,
                                                       @RequestParam(value = "confirmPassword") String confirmPassword)
            throws ValidationException, UserNotFoundException {

        userService.changePassword(email, password, confirmPassword);
        return response(HttpStatus.OK, "Password was successfully changed");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HttpResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest)
            throws UserNotFoundException {

        userService.resetPassword(resetPasswordRequest.getEmail());
        return response(HttpStatus.OK, "Password was successfully changed");
    }

    private HttpHeaders getJwtHeader(String jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Security.JWT_TOKEN_HEADER, jwt);
        return httpHeaders;
    }

}

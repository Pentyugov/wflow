package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.dto.UserDto;
import com.pentyugov.wflow.core.repository.UserRepository;
import com.pentyugov.wflow.core.service.*;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.payload.request.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.File.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;

@Service(UserService.NAME)
public class UserServiceImpl extends AbstractService implements UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${spring.mail.send-greeting}")
    private boolean sendGreetingEmail;

    @Value("${source.service.auth}")
    private String sourcePath;

    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ImageService imageService;
    private final ApplicationService applicationService;

    public UserServiceImpl(RoleService roleService,
                           BCryptPasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           EmailService emailService,
                           ImageService imageService, ApplicationService applicationService) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.imageService = imageService;
        this.applicationService = applicationService;
    }

    public User createUser(SignUpRequest userIn) throws UsernameExistException, EmailExistException, UsernameIsEmptyException, EmailIsEmptyException {
        if (validateUsernameAndEmail(null, userIn.getUsername(), userIn.getEmail())) {
            User user = new User();
            user.setUsername(userIn.getUsername());
            user.setPassword(passwordEncoder.encode(userIn.getPassword()));
            user.setEmail(userIn.getEmail());
            user.setFirstName(userIn.getFirstname());
            user.setLastName(userIn.getLastname());
            user.setActive(true);
            user.setNotLocked(true);
            user.getRoles().add(roleService.getRoleByName(Role.USER));
            user.setProfileImageUrl(getTemporaryProfileImageUrl(user.getUsername()));

            LOG.info("Registering new USER {} : {}", user.getUsername(), user.getEmail());
            User savedUser = userRepository.save(user);
            if (sendGreetingEmail) {
                emailService.sendRegisterSuccessMail(user, userIn.getPassword());
            }
            return savedUser;
        }
        return null;
    }

    public User addNewUser(UserDto userDto, String profileImage) throws UsernameExistException, EmailExistException, UsernameIsEmptyException, EmailIsEmptyException {
        validateUsernameAndEmail(null, userDto.getUsername(), userDto.getEmail());
        User user = createUserFromDto(userDto);
        String rawPassword = applicationService.generatePassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        LOG.info("Adding new USER {} : {}", user.getUsername(), user.getEmail());
        if (StringUtils.hasText(profileImage)) {
            user.setProfileImageUrl(profileImage);
        } else {
            user.setProfileImageUrl(TEMP_PROFILE_IMAGE_BASE_URL + user.getUsername());
        }
        user = userRepository.save(user);
        userDto.setProfileImage(user.getProfileImageUrl());
        userDto.setId(user.getId());
        if (sendGreetingEmail) {
            emailService.sendRegisterSuccessMail(user, rawPassword);
        }
        return user;
    }

    public User updateLastLoginDate(User user) {
        user.setLastLoginDate(LocalDateTime.now());
        user.setLastLoginDateDisplay(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(UserDto userDto, String profileImageUrl) throws UsernameExistException, EmailExistException, UserNotFoundException, UsernameIsEmptyException, EmailIsEmptyException {
        User currentUser = getUserById(userDto.getId());
        validateUsernameAndEmail(currentUser, userDto.getUsername(), userDto.getEmail());
        currentUser = updateUserFromDto(currentUser, userDto);

        LOG.info("Updating USER {} : {}", currentUser.getUsername(), currentUser.getEmail());
        if (StringUtils.hasText(profileImageUrl)) {
            if (currentUser.getProfileImageUrl().startsWith(PROFILE_IMAGE_RESOURCE_HOST)) {
                String oldImage = currentUser.getProfileImageUrl().split(PROFILE_IMAGE_RESOURCE_HOST)[1].replace("/", "");
                try {
                    UUID oldImageId = UUID.fromString(oldImage);
                    imageService.deleteUserProfileImage(oldImageId);
                } catch (IllegalArgumentException e) {
                    LOG.error(e.getMessage());
                }
            }

            currentUser.setProfileImageUrl(profileImageUrl);
        }
        currentUser = userRepository.save(currentUser);
        userDto.setProfileImage(currentUser.getProfileImageUrl());
        return currentUser;

    }

    public User updateUser(UserDto userDto) throws UsernameExistException, EmailExistException, UserNotFoundException, UsernameIsEmptyException, EmailIsEmptyException {
        User currentUser = getUserById(userDto.getId());
        validateUsernameAndEmail(currentUser, userDto.getUsername(), userDto.getEmail());
        currentUser = updateUserFromDto(currentUser, userDto);

        LOG.info("Updating USER {} : {}", currentUser.getUsername(), currentUser.getEmail());

        if (!StringUtils.hasText(userDto.getFirstName())) {
            currentUser.setFirstName(null);
        }
        if (!StringUtils.hasText(userDto.getLastName())) {
            currentUser.setLastName(null);
        }

        if (StringUtils.hasText(userDto.getProfileImage())) {
            if (currentUser.getProfileImageUrl().startsWith(PROFILE_IMAGE_RESOURCE_HOST)) {
                String oldImage = currentUser.getProfileImageUrl().split(PROFILE_IMAGE_RESOURCE_HOST)[1].replace("/", "");
                try {
                    UUID oldImageId = UUID.fromString(oldImage);
                    imageService.deleteUserProfileImage(oldImageId);
                } catch (IllegalArgumentException e) {
                    LOG.error(e.getMessage());
                }
            }

            currentUser.setProfileImageUrl(userDto.getProfileImage());
        }
        currentUser = userRepository.save(currentUser);
        userDto.setProfileImage(currentUser.getProfileImageUrl());
        return currentUser;

    }

    public void deleteUser(UUID id) {
        userRepository.delete(id);
    }

    public void resetPassword(String email) throws UserNotFoundException {
        User user = getUserByEmail(email);
        String rawPassword = applicationService.generatePassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
        emailService.sentResetPasswordMail(user, rawPassword);
    }

    public void changePassword(String email, String password, String confirmPassword, Principal principal) throws UserNotFoundException, ValidationException {
        User user;
        if (StringUtils.hasText(email)) {
            user = userRepository.findByEmail(email).orElseThrow(() ->
                    new UserNotFoundException(getMessage(sourcePath, "exception.user.with.email.not.found", email)));
        } else {
            user = getUserByPrincipal(principal);
        }
        if (password.length() < 8) {
            throw new ValidationException("Password must contain at least 8 characters");
        } else if (!password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match");
        } else if (passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("The password must not be the same as the previous one");
        } else {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    public User updateProfileImage(UUID id, String profileImageUrl) throws UserNotFoundException {
        User user = getUserById(id);
        if (StringUtils.hasText(profileImageUrl)) {
            if (StringUtils.hasText(user.getProfileImageUrl()) &&
                    user.getProfileImageUrl().startsWith(PROFILE_IMAGE_RESOURCE_HOST)) {
                String oldImage = user.getProfileImageUrl().split(PROFILE_IMAGE_RESOURCE_HOST)[1].replace("/", "");
                try {
                    UUID oldImageId = UUID.fromString(oldImage);
                    imageService.deleteUserProfileImage(oldImageId);
                } catch (IllegalArgumentException e) {
                    LOG.error(e.getMessage());
                }
            }

            user.setProfileImageUrl(profileImageUrl);
        }
        userRepository.save(user);
        return user;
    }

    public User getCurrentUser(Principal principal) throws UserNotFoundException {
        return getUserByPrincipal(principal);
    }

    public User getUserById(UUID id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(getMessage(sourcePath, "exception.user.with.id.not.found", id.toString())));
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(getMessage(sourcePath, "exception.user.with.email.not.found", email)));
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(getMessage(sourcePath, "exception.user.with.username.not.found", username)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllWithRole(String roleName) {
        return userRepository.finAllByRole(roleName.toUpperCase());
    }

    @Override
    public List<User> getAllWithPermission(String permission) {
        return userRepository.finAllByPermission(permission.toUpperCase());
    }

    public List<User> getUsersWithEmployee() {
        return userRepository.findWithEmployee().stream().collect(Collectors.toList());
    }

    public List<User> getUsersWithoutEmployee() {
        List<UUID> ids = getUsersWithEmployee().stream().map(User::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ids)) {
            return userRepository.findWithoutEmployee(ids);
        }
        return getAllUsers();
    }

    public Page<User> getPageOfUsers(int page, int size, Sort.Direction direction, String sortBy) {
        return userRepository.findAll(PageRequest.of(page, size, direction, sortBy));
    }

    public User createUserFromDto(UserDto userDto) {
        User user = new User();
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNotLocked(userDto.isNotLocked());
        user.setActive(userDto.isActive());
        user.setJoinDate(LocalDateTime.now());

        Set<Role> roles = new HashSet<>();
        userDto.getRoles().forEach(roleDto ->
                roles.add(roleService.getRoleById(roleDto.getId())));
        if (CollectionUtils.isEmpty(roles)) {
            user.getRoles().add(getStandardRole());
        } else {
            user.setRoles(roles);
        }

        return user;
    }

    public UserDto createUserDto(UUID id,
                                 String username,
                                 String email,
                                 String firstName,
                                 String lastName,
                                 List<RoleDto> roles,
                                 String isActive,
                                 String isNonLocked) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setUsername(username);
        userDto.setEmail(email);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setRoles(roles);
        userDto.setActive(Boolean.parseBoolean(isActive));
        userDto.setNotLocked(Boolean.parseBoolean(isNonLocked));
        return userDto;
    }

    public UserDto createUserDtoFromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setNotLocked(user.isNotLocked());
        userDto.setActive(user.isActive());
        userDto.setJoinDate(user.getJoinDate());
        userDto.setLastLoginDate(user.getLastLoginDate());
        userDto.setLastLoginDateDisplay(user.getLastLoginDateDisplay());
        userDto.setProfileImage(user.getProfileImageUrl());

        List<RoleDto> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(roleService.createProxyFromRole(role)));
        userDto.setRoles(roles);

        return userDto;
    }

    public User updateUserFromDto(User currentUser, UserDto userDto) {
        currentUser.setUsername(userDto.getUsername());
        currentUser.setEmail(userDto.getEmail());
        currentUser.setFirstName(userDto.getFirstName());
        currentUser.setLastName(userDto.getLastName());
        currentUser.setNotLocked(userDto.isNotLocked());
        currentUser.setActive(userDto.isActive());

        Set<Role> roles = new HashSet<>();
        userDto.getRoles().forEach(roleDto -> roles.add(roleService.getRoleById(roleDto.getId())));
        if (CollectionUtils.isEmpty(roles)) {
            roles.add(getStandardRole());
        }
        currentUser.setRoles(roles);

        return currentUser;
    }

    private Role getStandardRole() {
        return roleService.getRoleByName(Role.USER);
    }

    private boolean validateUsernameAndEmail(User currentUser, String username, String email) throws UsernameExistException, EmailExistException, UsernameIsEmptyException, EmailIsEmptyException {
        User checkedUser;
        if (!StringUtils.hasText(username)) {
            throw new UsernameIsEmptyException(getMessage(sourcePath, "exception.username.empty", null));
        }

        if (!StringUtils.hasText(email)) {
            throw new EmailIsEmptyException(getMessage(sourcePath, "exception.email.empty", null));
        }
        if (ObjectUtils.isEmpty(currentUser)) {
            checkedUser = userRepository.findByUsername(username).orElse(null);
            if (!ObjectUtils.isEmpty(checkedUser)) {
                throw new UsernameExistException(getMessage(sourcePath, "exception.user.with.username.exist", username));
            }

            checkedUser = userRepository.findByEmail(email).orElse(null);
            if (!ObjectUtils.isEmpty(checkedUser)) {
                throw new EmailExistException(getMessage(sourcePath, "exception.user.with.email.exist", email));
            }
        } else {
            checkedUser = userRepository.findByUsername(username).orElse(null);
            if (!ObjectUtils.isEmpty(checkedUser) && !currentUser.getId().equals(checkedUser.getId())) {
                throw new UsernameExistException(getMessage(sourcePath, "exception.user.with.username.exist", username));
            }

            checkedUser = userRepository.findByEmail(email).orElse(null);
            if (!ObjectUtils.isEmpty(checkedUser) && !currentUser.getId().equals(checkedUser.getId())) {
                throw new EmailExistException(getMessage(sourcePath, "exception.user.with.email.exist", email));
            }
        }

        return true;

    }

    public User getUserByPrincipal(Principal principal) throws UserNotFoundException {
        String username = principal.getName();
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User with username {" + username + "} not found"));
    }

    public boolean isUserAdmin(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(Role.ADMIN));
    }

    @Override
    public boolean isUserInRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + "/temp/" + username).toUriString();
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (!ObjectUtils.isEmpty(profileImage)) {

            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }

            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOG.info(DIRECTORY_CREATED + userFolder);
            }

            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);

            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOG.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(API_PREFIX + USER_IMAGE_PATH + username +
                FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
    }

    private List<String> getExistingRolesNames(String namesFromRequest) {
        String prepared = namesFromRequest.toUpperCase().replace(" ", "");
        List<Role> existingRoles = roleService.getRolesByName(Arrays.asList(prepared.split("\\s*;\\s*")));
        if (CollectionUtils.isEmpty(existingRoles)) {
            existingRoles = Collections.singletonList(getStandardRole());
        }
        return existingRoles.stream().map(Role::getName).collect(Collectors.toList());
    }


    public User deleteUserProfileImage(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(user) && user.getProfileImageUrl().startsWith(PROFILE_IMAGE_RESOURCE_HOST)) {
            String oldImage = user.getProfileImageUrl().split(PROFILE_IMAGE_RESOURCE_HOST)[1].replace("/", "");
            try {
                UUID oldImageId = UUID.fromString(oldImage);
                imageService.deleteUserProfileImage(oldImageId);
            } catch (IllegalArgumentException e) {
                LOG.error(e.getMessage());
            }
            user.setProfileImageUrl(TEMP_PROFILE_IMAGE_BASE_URL + user.getUsername());
        }
        return userRepository.save(user);
    }

    public void removeUserByUsername(String username) {
        userRepository.removeUserByUsername(username);
    }
}

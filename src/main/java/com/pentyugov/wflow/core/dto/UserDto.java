package com.pentyugov.wflow.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pentyugov.wflow.core.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto extends BaseDto {

    @NotEmpty
    private String username;

    @NotEmpty
    @ValidEmail
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private List<RoleDto> roles = new ArrayList<>();
    private boolean nonLocked;
    private boolean active;
    private String profileImage;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastLoginDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastLoginDateDisplay;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime joinDate;

}

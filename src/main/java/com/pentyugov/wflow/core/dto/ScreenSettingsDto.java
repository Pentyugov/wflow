package com.pentyugov.wflow.core.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScreenSettingsDto {

    private String screen;
    private String[] itemsPerPage;

}

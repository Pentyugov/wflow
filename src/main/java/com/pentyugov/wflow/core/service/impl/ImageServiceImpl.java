package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.File.PROFILE_IMAGE_DELETE_API;
import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Security.AUTHORIZATION;

@Service(ImageService.NAME)
public class ImageServiceImpl extends AbstractService implements ImageService {

    @Value("${uploadcare.scheme}")
    private String scheme;
    @Value("${uploadcare.public-key}")
    private String publicKey;
    @Value("${uploadcare.secret-key}")
    private String secretKey;

    public static final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/temp";

    public String getTmpImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH).toUriString();
    }

    public void deleteUserProfileImage(UUID imageId) {
        String resource = PROFILE_IMAGE_DELETE_API + imageId.toString() + "/";
        String authorizationValue = scheme + " " + publicKey + ":" + secretKey;
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, authorizationValue);
        HttpEntity request = new HttpEntity(headers);
        template.exchange(resource, HttpMethod.DELETE, request, Object.class);
    }
}

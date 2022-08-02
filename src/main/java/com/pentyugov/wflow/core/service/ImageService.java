package com.pentyugov.wflow.core.service;

import java.util.UUID;

public interface ImageService {
    String NAME = "wflow$ImageService";

    void deleteUserProfileImage(UUID imageId);
}

package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.UserSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserSettingsRepository extends BaseRepository<UserSettings> {

    @Transactional(readOnly = true)
    @Query("select us from security$UserSettings us where us.user.id = ?1")
    Optional<UserSettings> findUserSettingsForUser(UUID userId);
}

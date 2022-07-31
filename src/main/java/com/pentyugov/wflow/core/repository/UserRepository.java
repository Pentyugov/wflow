package com.pentyugov.wflow.core.repository;


import com.pentyugov.wflow.core.domain.entity.BaseEntity;
import com.pentyugov.wflow.core.domain.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User> {

    @Override
    @Transactional(readOnly = true)
    @Query("select u from security$User u order by u.username asc")
    List<User> findAll();

    @Transactional(readOnly = true)
    @Query("select u from security$User u where u.username = ?1 and u.deleteDate is null")
    Optional<User> findByUsername(String username);

    @Transactional(readOnly = true)
    @Query("select u from security$User u where u.email = ?1 and u.deleteDate is null")
    Optional<User> findByEmail(String email);

    @Transactional(readOnly = true)
    @Query("select e.user from workflow$Employee e")
    Optional<User> findWithEmployee();

    @Transactional(readOnly = true)
    @Query("select u from security$User u where u.id not in :ids order by u.username asc")
    List<User> findWithoutEmployee(List<UUID> ids);

    @Transactional(readOnly = true)
    @Query("select u from security$User u join u.roles r where r.name LIKE CONCAT('%',:roleName,'%')")
    List<User> findAllByRole(@Param("roleName")String roleName);

    @Transactional(readOnly = true)
    @Query("select distinct u from security$User u inner join u.roles r where r.name IN (:roleNames)")
    List<User> findAllInAnyRole(@Param("roleNames") List<String> roleName);

    @Transactional(readOnly = true)
    @Query("select u from security$User u join u.roles r join r.permissions p where p.name LIKE CONCAT('%',:permission,'%')")
    List<User> findAllByPermission(@Param("permission")String permission);

    @Transactional(readOnly = true)
    @Query("select u from security$User u where u.telChatId is not null and u.telUserId is not null and u.telLogged = TRUE")
    List<User> findAllLoggedInTelegram();

    @Transactional(readOnly = true)
    @Query("select u from security$User u where u.telUserId = ?1")
    Optional<User> findUserByTelUserId(Long telUserId);

    @Query("update #{#entityName} e set e.deleteDate = current_timestamp where e.id = ?1")
    @Transactional
    @Modifying
    void delete(UUID id);

    @Override
    @Transactional
    default void delete(User user) {
        user.setActive(false);
        delete(user.getId());
    }

    @Transactional
    default void delete(Iterable<? extends BaseEntity> entities) {
        entities.forEach(entity -> delete((User) entity));
    }

    @Modifying
    @Transactional
    @Query("delete from security$User u where u.username = ?1")
    void removeUserByUsername(String username);
}

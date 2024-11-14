package com.ms.user.repositories;

import com.ms.user.models.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

@Transactional
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    @Query("SELECT t FROM UserModel t WHERE t.name = :name")
    UserDetails findUserDetailsByName(String name);

    @Query("SELECT u FROM UserModel u WHERE u.name = :name")
    Optional<UserModel> findByName(String name);

}

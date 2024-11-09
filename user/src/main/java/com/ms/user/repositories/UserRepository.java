package com.ms.user.repositories;

import com.ms.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserModel, UUID> {

    @Query("SELECT t from UserModel t where t.name = :name and t.senha = :senha")
    Optional<UserModel> findByNameAndSenha(String name, String senha);

    @Modifying
    @Query("UPDATE UserModel set codeTemporario = :codigo where name = :name and senha = :senha")
    void updateByCodeTemporario(String codigo, String name, String senha);
}

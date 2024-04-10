package com.userauth.userauthenticate.repository;

import com.userauth.userauthenticate.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByValueAndDeletedEquals(String token, boolean Deleted);

    Optional<Token> findByValueAndDeletedEqualsAndExpireAtGreaterThan(String token, boolean Deleted, Date date);
}

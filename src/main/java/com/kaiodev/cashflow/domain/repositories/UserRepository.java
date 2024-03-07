package com.kaiodev.cashflow.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.kaiodev.cashflow.domain.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByEmail(String email);
}

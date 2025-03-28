package com.adminuser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminuser.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
package com.example.Orders.Microservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Orders.Microservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

package com.example.Orders.Microservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Orders.Microservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}

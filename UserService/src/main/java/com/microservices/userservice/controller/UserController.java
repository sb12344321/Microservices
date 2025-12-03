package com.microservices.userservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.microservices.userservice.feign.OrderClient;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private OrderClient orderClient;

    @GetMapping("/{userId}/orders")
    @CircuitBreaker(name = "orderServiceBreaker", fallbackMethod = "fallbackOrders")
    public String getUserOrders(@PathVariable String userId) {
        return orderClient.getOrdersByUser(userId);
    }

    public String fallbackOrders(String userId, Throwable t) {
        return "Fallback: Order Service unavailable.";
    }
}

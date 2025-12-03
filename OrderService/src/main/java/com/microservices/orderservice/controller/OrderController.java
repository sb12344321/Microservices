package com.microservices.orderservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/user/{userId}")
    public String getOrdersByUser(@PathVariable String userId) {
        return "Orders for user: " + userId;
    }
}

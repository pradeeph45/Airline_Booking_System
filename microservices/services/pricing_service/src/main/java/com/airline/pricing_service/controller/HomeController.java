package com.airline.pricing_service.controller;

import com.airline.payload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse homeController(){
        ApiResponse apiResponse = new ApiResponse("Hello From Pricing service");
        return apiResponse;
    }
}

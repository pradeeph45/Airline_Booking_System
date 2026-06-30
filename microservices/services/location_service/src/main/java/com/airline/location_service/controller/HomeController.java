package com.airline.location_service.controller;

import com.airline.payload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/hello")
    public ApiResponse homeController(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Hello I am location service");
        return apiResponse;
    }
}

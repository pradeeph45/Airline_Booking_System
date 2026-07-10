package com.airline.airline_core_service.controller;

import com.airline.payload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse homeController(){
        ApiResponse apiResponse = new ApiResponse(
                "Hi, This is airline core service"
        );
        return apiResponse;
    }
}

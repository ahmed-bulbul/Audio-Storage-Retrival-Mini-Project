package com.audio.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @Operation(summary = "Home page")
    @GetMapping
    public String home() {
        return "Audio Storage Service.";
    }

}

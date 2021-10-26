package com.hw.payAPI.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class WebController {
    @GetMapping("/home")
    public String homepage() {
        return "index.html";
    }
}

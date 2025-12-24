package com.plantdisease.frontend_springboot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import com.plantdisease.frontend_springboot.service.PredictionService;

@Controller
public class DiseasePredController {

    @Autowired
    private PredictionService service;

    @GetMapping
    public String getHome() {
        return "home";
    }

    @GetMapping("/upload")
    public String getUpload() {
        return "upload";
    }

    @PostMapping("/predict")
    public String predict(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Map<String, Object> result = service.predict(file);
            
            // Check if the result map contains an 'error' key returned by Flask
            if (result.containsKey("error")) {
                model.addAttribute("error", result.get("error"));
            } else {
                model.addAttribute("result", result);
            }
            return "upload";
        }
        catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Could not connect to the analysis server.");
            return "upload";
        }
    }

}
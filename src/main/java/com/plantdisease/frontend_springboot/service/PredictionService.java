package com.plantdisease.frontend_springboot.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PredictionService {
    
    @Value("${backend.base.url}")
    private String backendUrl;

    private static final String BASE_URL = "/api/predict";

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> predict(MultipartFile file) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // USE ByteArrayResource here too!
        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(backendUrl + BASE_URL, request, Map.class);
    } catch (Exception e) {
        throw new RuntimeException("Frontend failed to reach Backend: " + e.getMessage());
    }
}

}

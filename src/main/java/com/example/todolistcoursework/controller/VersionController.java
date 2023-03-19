package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.CurrentVersionResponse;
import com.example.todolistcoursework.util.PropertiesReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class VersionController {
    private final String version;

    public VersionController(@Value("${pom.propertiesFileName}") String pomPropertiesFile) throws IOException {
        version = new PropertiesReader(pomPropertiesFile)
                .getProperty("product.version");
    }

    @GetMapping("/version")
    public ResponseEntity<CurrentVersionResponse> getCurrentVersion() {
        return ResponseEntity.ok(new CurrentVersionResponse(version));
    }
}

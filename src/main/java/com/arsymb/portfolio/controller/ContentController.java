package com.arsymb.portfolio.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/contents")
public class ContentController {

    private static Logger logger = LogManager.getLogger(ContentController.class);

    ObjectMapper mapper = new ObjectMapper();

    Map<String, Object> content = new HashMap<>();

    @GetMapping("/index")
    public ResponseEntity<String> getIndexContents() {
        String title = "ContentControllerGetIndexContents/";

        getContent();

        Map<String, Object> index = new HashMap<>();
        try {
            index = mapper.convertValue(content.get("index"), new TypeReference<Map<String, Object>>() {
            });
            if (index != null && !index.isEmpty()) {
                byte[] imgBytes = Files.readAllBytes(Paths.get(index.get("imgSrc").toString()));
                index.put("imgSrc", Base64.getEncoder().encodeToString(imgBytes));
            }
            return ResponseEntity.status(HttpStatus.OK).body(encode(index));
        } catch (Exception e) {
            logger.warn(title + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(encode(index));
        }
    }

    @GetMapping("/about")
    public ResponseEntity<String> getAboutContents() {
        String title = "ContentControllerGetAboutContents/";

        getContent();

        Map<String, Object> about = new HashMap<>();
        try {
            about = mapper.convertValue(content.get("about"), new TypeReference<Map<String, Object>>() {
            });
            if (about != null && !about.isEmpty()) {
                byte[] imgBytes = Files.readAllBytes(Paths.get(about.get("imgSrc").toString()));
                about.put("imgSrc", Base64.getEncoder().encodeToString(imgBytes));
            }
            return ResponseEntity.status(HttpStatus.OK).body(encode(about));
        } catch (Exception e) {
            logger.warn(title + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(encode(about));
        }
    }

    @GetMapping("/project")
    public ResponseEntity<String> getProjectContents() {
        String title = "ContentControllerGetProjectContents/";

        getContent();

        List<Map<String, Object>> projects = new ArrayList<>();
        try {
            projects = mapper.convertValue(content.get("projects"), new TypeReference<List<Map<String, Object>>>() {
            });
            return ResponseEntity.status(HttpStatus.OK).body(encode(projects));
        } catch (Exception e) {
            logger.warn(title + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(encode(projects));
        }
    }

    @GetMapping("/education")
    public ResponseEntity<String> getEducationContents() {
        String title = "ContentControllerGetEducationContents/";

        getContent();

        List<Map<String, Object>> educations = new ArrayList<>();
        try {
            educations = mapper.convertValue(content.get("educations"), new TypeReference<List<Map<String, Object>>>() {
            });
            return ResponseEntity.status(HttpStatus.OK).body(encode(educations));
        } catch (Exception e) {
            logger.warn(title + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(encode(educations));
        }
    }

    @GetMapping("/certification")
    public ResponseEntity<String> getCertificationContents() {
        String title = "ContentControllerGetCertificationContents/";

        getContent();

        List<Map<String, Object>> certifications = new ArrayList<>();
        try {
            certifications = mapper.convertValue(content.get("certifications"),
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            if (certifications != null && !certifications.isEmpty()) {
                certifications.forEach(c -> {
                    try {
                        byte[] imgBytes = Files.readAllBytes(Paths.get(c.get("imgSrc").toString()));
                        c.put("imgSrc", Base64.getEncoder().encodeToString(imgBytes));
                    } catch (IOException e) {
                        logger.warn(title + "Image Loading Loop - Error {}", e.getMessage());
                    }
                });
            }

            return ResponseEntity.status(HttpStatus.OK).body(encode(certifications));
        } catch (Exception e) {
            logger.warn(title + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(encode(certifications));
        }
    }

    private String encode(Object objToEncode) {
        String title = "ContentControllerEncode/";
        try {
            String strToEncode = mapper.writeValueAsString(objToEncode);
            return Base64.getEncoder().encodeToString(strToEncode.getBytes());
        } catch (JsonProcessingException e) {
            logger.warn(title + e.getStackTrace());
            return "";
        }
    }

    private void getContent() {
        String title = "ContentControllerGetContent/";
        try {
            if (content != null && !content.isEmpty())
                return;
            InputStream is = Files.newInputStream(Paths.get("../assets/conf/content.json"));
            content = mapper.readValue(is, Map.class);
        } catch (Exception e) {
            logger.warn(title + e.getMessage());
        }
    }

}

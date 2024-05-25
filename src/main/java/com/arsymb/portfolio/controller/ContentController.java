package com.arsymb.portfolio.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
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

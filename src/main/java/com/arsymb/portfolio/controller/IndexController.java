package com.arsymb.portfolio.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    private static Logger logger = LogManager.getLogger(IndexController.class);

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/about")
    public String showAbout() {
        return "about";
    }

    @GetMapping("/download-resume")
    public void downloadResume(HttpServletResponse response) {
        String title = "IndexControllerDownloadResume";

        try {
            InputStream streamResume = Files.newInputStream(Paths.get("../assets/docs/resume.pdf"));
            IOUtils.copy(streamResume, response.getOutputStream());
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment;filename=" + "Resume" + ".pdf");
            response.flushBuffer();
        } catch (Exception e) {
            logger.warn(title + e.getStackTrace());
        }
    }

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

}

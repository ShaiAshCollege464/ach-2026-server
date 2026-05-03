package com.social.controllers;

import com.social.Entity.*;
import com.social.responses.BasicResponse;
import com.social.responses.PostResponse;
import com.social.responses.UserResponse;
import com.social.utils.DbUtils;
import com.social.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@RestController
public class GeneralController {

    @PostConstruct
    public void init() {
                this.persist = persist;

    }
    private String generateMD5(String username, String password) {
        try {
            String source = username + password;

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(source.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

   
    @PostMapping("/register") 
    public BasicResponse register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "photo", required = false) MultipartFile photo 
    ) {
        String hashedPassword = generateMD5(username, password);
        return dbUtils.registerUser(username, hashedPassword, savedPath);
    }

   

    @RequestMapping(value = "/Login")
    public UserResponse login(@RequestParam("username") String username, 
                              @RequestParam("password") String password) {
       // String hashedPassword = generateMD5(username, password);
       String hashedPassword = password;
        UserResponse result = this.persist.login(username, hashedPassword);
        return result;
    }


}


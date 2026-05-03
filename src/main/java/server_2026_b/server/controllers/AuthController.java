package server_2026_b.server.controllers;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import server_2026_b.server.entities.TeamEntity;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.TeamModel;
import server_2026_b.server.responses.UserResponse;
import server_2026_b.server.service.Persist;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



@RestController
public class AuthController {
    private final Persist persist;

    public AuthController(Persist persist) {
        this.persist = persist;
    }

    @PostConstruct
    public void init() {

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
    @RequestMapping("/register")
    public void register(
             String username,String password) {
        System.out.println("test");
//        String hashedPassword = generateMD5(username, password);
        String hashedPassword = password;
        this.persist.registerUser(username, hashedPassword);
    }
    @RequestMapping(value = "/Login")
    public UserResponse login(String username, String password) {
       // String hashedPassword = generateMD5(username, password);
       String hashedPassword = password;
        UserResponse result = this.persist.login(username, hashedPassword);
        return result;
    }


}


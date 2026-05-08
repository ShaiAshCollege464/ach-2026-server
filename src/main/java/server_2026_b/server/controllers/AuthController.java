package server_2026_b.server.controllers;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server_2026_b.server.entities.TeamEntity;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.TeamModel;
import server_2026_b.server.responses.UserResponse;
import server_2026_b.server.service.Persist;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
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
    public static String generateMD5(String username, String password) {
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
    public UserResponse login(String username, String password, HttpServletResponse response) {
        String hashedPassword = generateMD5(username, password);
        UserResponse result = this.persist.login(username, hashedPassword);
        if (result != null && result.isSuccess()) {
            StringBuilder stringBuilder = new StringBuilder("token=")
                    .append(result.getUser().getToken())
                    .append("; Path=/")
                    .append("; Max-Age=").append(60 * 60 * 24)
                    .append("; HttpOnly");
            response.addHeader("Set-Cookie", stringBuilder.toString());
        }
        return result;
    }

    @RequestMapping("/me")
    public BasicResponse me(@CookieValue("token") String token) {
        return new BasicResponse(token != null && !token.isEmpty(), null);
    }



}


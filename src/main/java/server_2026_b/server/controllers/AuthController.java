package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;

import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.UserResponse;
import server_2026_b.server.service.Persist;

import server_2026_b.server.entities.UserEntity;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.AuthenticatorResponse;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.UserResponse;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.TotpUtils;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;




@RestController

import static server_2026_b.server.utils.TotpUtils.getCurrentCodeFromBase32;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class AuthController extends BasicController 
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

        String secret = TotpUtils.toBase32(username);
        String otp = getCurrentCodeFromBase32(secret);
        System.out.println("--- TOTP DEBUG ---");
        System.out.println("Username: " + username);
        System.out.println("Secret (Base32): " + secret);
        System.out.println("Server generated OTP: " + otp);
        System.out.println("User entered password/OTP: " + password);
        System.out.println("------------------");
        UserResponse result = this.persist.login(username, "");
        if (result != null && result.isSuccess() && otp.equals(password)) {

            StringBuilder stringBuilder = new StringBuilder("token=")
                    .append(result.getUser().getToken())
                    .append("; Path=/")
                    .append("; Max-Age=").append(60 * 60 * 24)
                    .append("; HttpOnly");
            response.addHeader("Set-Cookie", stringBuilder.toString());
            return result;
        } else {
            return null;
        }
    }

    @RequestMapping("/me")
    public BasicResponse me(@CookieValue("token") String token) {
        return new BasicResponse(token != null && !token.isEmpty(), null, null);
    }



    @RequestMapping("/get-authenticator-uri")
    public BasicResponse getAuthenticatorUri(@CookieValue(value = "token") String token) {
        WorkerEntity manager = persist.getWorkerByToken(token);
        UserEntity user = persist.getUserByWorker(manager);
        if (user.isShouldDisplayQr()) {
            String secret = TotpUtils.toBase32(manager.getFirstName() + " " + manager.getLastName());
            String uri =
                    String.format("otpauth://totp/MyWorkersApp?secret=%s&issuer=MyWorkersApp&digits=8", secret);
            user.setShouldDisplayQr(false);
            persist.save(user);
            return new AuthenticatorResponse(true, null, uri);
        } else {
            return new AuthenticatorResponse(true, null, "");
        }
    }


    public static void main(String[] args) {
        String secret = TotpUtils.toBase32("SHAI_GIVATI");
        System.out.println(getCurrentCodeFromBase32(secret));
    }


}


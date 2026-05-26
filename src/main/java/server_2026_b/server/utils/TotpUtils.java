package server_2026_b.server.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;

public class TotpUtils {
    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int SECRET_SIZE_BYTES = 20;
    private static final int CODE_DIGITS = 6;
    private static final int TIME_PERIOD_SECONDS = 30;
    private static final int VALIDATION_WINDOW = 1;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();


    public static String toBase32(String input) {
        if (input == null) {
            return null;
        }
        return encodeBase32(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String getCurrentCodeFromBase32(String base32Secret) {
        if (isBlank(base32Secret)) {
            return null;
        }
        try {
            byte[] secretBytes = decodeBase32(base32Secret.trim());
            long counter = Instant.now().getEpochSecond() / TIME_PERIOD_SECONDS;
            return generateTotpCode(secretBytes, counter);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String generateSecret() {
        byte[] randomBytes = new byte[SECRET_SIZE_BYTES];
        SECURE_RANDOM.nextBytes(randomBytes);
        return encodeBase32(randomBytes);
    }


    public static boolean verifyCode(String secret, String otpCode) {
        if (isBlank(secret) || isBlank(otpCode)) {
            return false;
        }
        String normalizedCode = otpCode.trim();
        if (!normalizedCode.matches("\\d{6}")) {
            return false;
        }
        try {
            byte[] secretBytes = decodeBase32(secret.trim());
            long currentCounter = Instant.now().getEpochSecond() / TIME_PERIOD_SECONDS;
            for (int offset = -VALIDATION_WINDOW; offset <= VALIDATION_WINDOW; offset++) {
                String generated = generateTotpCode(secretBytes, currentCounter + offset);
                if (generated.equals(normalizedCode)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static String generateTotpCode(byte[] secretBytes, long counter) throws Exception {
        byte[] counterBytes = new byte[8];
        long value = counter;
        for (int i = 7; i >= 0; i--) {
            counterBytes[i] = (byte) (value & 0xFF);
            value >>= 8;
        }

        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(secretBytes, "HmacSHA1");
        mac.init(keySpec);
        byte[] hash = mac.doFinal(counterBytes);

        int offset = hash[hash.length - 1] & 0x0F;
        int binary = ((hash[offset] & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                | (hash[offset + 3] & 0xFF);
        int otp = binary % (int) Math.pow(10, CODE_DIGITS);
        return String.format("%0" + CODE_DIGITS + "d", otp);
    }

    private static String encodeBase32(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int buffer = bytes[0] & 0xFF;
        int next = 1;
        int bitsLeft = 8;
        while (bitsLeft > 0 || next < bytes.length) {
            if (bitsLeft < 5) {
                if (next < bytes.length) {
                    buffer <<= 8;
                    buffer |= bytes[next++] & 0xFF;
                    bitsLeft += 8;
                } else {
                    int pad = 5 - bitsLeft;
                    buffer <<= pad;
                    bitsLeft += pad;
                }
            }
            int index = (buffer >> (bitsLeft - 5)) & 0x1F;
            bitsLeft -= 5;
            result.append(BASE32_ALPHABET.charAt(index));
        }
        return result.toString();
    }

    private static byte[] decodeBase32(String base32) {
        String normalized = base32
                .replace("=", "")
                .replace(" ", "")
                .toUpperCase(Locale.ROOT);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int buffer = 0;
        int bitsLeft = 0;

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            int val = BASE32_ALPHABET.indexOf(c);
            if (val < 0) {
                throw new IllegalArgumentException("Invalid Base32 character");
            }
            buffer = (buffer << 5) | val;
            bitsLeft += 5;
            if (bitsLeft >= 8) {
                out.write((buffer >> (bitsLeft - 8)) & 0xFF);
                bitsLeft -= 8;
            }
        }
        return out.toByteArray();
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

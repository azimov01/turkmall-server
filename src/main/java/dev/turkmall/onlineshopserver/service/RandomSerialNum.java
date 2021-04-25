package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class RandomSerialNum {
    @Autowired
    OrderRepository orderRepository;

    public String generateSerialNumber() {

        while (true) {
            String serial = randomSerial(10, 10, 'A');
            if (checkSerial(serial)) {
                return serial;
            }
        }
    }

    public boolean checkSerial(String serial) {
        if (!orderRepository.existsBySerialNumber(serial)) {
            return true;
        } else {
            return false;
        }
    }

    static final private String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    static char randomChar() {
        Random rng = new SecureRandom();
        return ALPHABET.charAt(rng.nextInt(ALPHABET.length()));
    }

    public static String randomSerial(int length, int spacing, char spacerChar) {
        StringBuilder sb = new StringBuilder();
        int spacer = 0;
        while (length > 0) {
            if (spacer == spacing) {
                sb.append(spacerChar);
                spacer = 0;
            }
            length--;
            spacer++;
            sb.append(randomChar());
        }
        return sb.toString();
    }
}

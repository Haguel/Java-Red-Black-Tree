package dev.haguel.service;

import java.util.Random;

public class Generator {
    public static final Random random;

    static {
        random = new Random();
    }

    public static int generateRandomInt(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }
}

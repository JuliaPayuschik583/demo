package com.example.demo;

public class Utils {

    private Utils() {
        throw new AssertionError("This class must not have Instance!");
    }

    public static long getUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }
}

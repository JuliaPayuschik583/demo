package com.example.demo;

public class Validator {

    private Validator() {
        throw new AssertionError("This class must not have Instance!");
    }

    public static void isNotNull(final Object o, final String errMessage) throws Exception {
        if (o == null) {
            throw new Exception(errMessage);
        }
    }
}

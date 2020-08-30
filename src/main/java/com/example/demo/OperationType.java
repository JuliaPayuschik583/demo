package com.example.demo;

public enum OperationType {
    PLUS(1), MINUS(0);

    private final int type;

    OperationType(final int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

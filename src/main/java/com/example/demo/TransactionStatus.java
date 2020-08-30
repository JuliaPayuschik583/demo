package com.example.demo;

public enum  TransactionStatus {
    START(0), SUCCESSFUL(1), ERROR(2);

    private final int status;

    TransactionStatus(final int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

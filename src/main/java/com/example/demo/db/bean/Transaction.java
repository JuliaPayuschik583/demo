package com.example.demo.db.bean;

public class Transaction {

    private int transactionId;
    private int fromParticipantId;
    private int toParticipantId;
    private int fromAccountId;
    private int toAccountId;
    private long date;
    private int status;
    private String message;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getFromParticipantId() {
        return fromParticipantId;
    }

    public void setFromParticipantId(int fromParticipantId) {
        this.fromParticipantId = fromParticipantId;
    }

    public int getToParticipantId() {
        return toParticipantId;
    }

    public void setToParticipantId(int toParticipantId) {
        this.toParticipantId = toParticipantId;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

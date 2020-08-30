package com.example.demo.db.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {

    private int accountId;
    private int participantId;
    private long amount;
    private String currency;

    public Account() {
    }

    public Account(ResultSet rs) throws SQLException {
        this.accountId = rs.getInt("account_id");
        this.participantId = rs.getInt("participant_id");
        this.amount = rs.getLong("amount");
        this.currency = rs.getString("currency");
        //rs.getTimestamp("created_date").toLocalDateTime()
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}

package com.example.demo.requests;

import com.example.demo.db.bean.Account;

public class AddAccountRequest {

    private Long participantId;
    private Account account;

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

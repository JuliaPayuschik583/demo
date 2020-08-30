package com.example.demo.requests;

public class InnerMoneyOrderRequest extends MoneyOrderRequest {

    private Long participantId;

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }
}

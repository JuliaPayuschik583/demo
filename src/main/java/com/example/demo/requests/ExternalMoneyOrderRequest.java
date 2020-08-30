package com.example.demo.requests;

public class ExternalMoneyOrderRequest extends MoneyOrderRequest {

    private Long fromParticipantId;
    private Long toParticipantId;

    public Long getFromParticipantId() {
        return fromParticipantId;
    }

    public void setFromParticipantId(Long fromParticipantId) {
        this.fromParticipantId = fromParticipantId;
    }

    public Long getToParticipantId() {
        return toParticipantId;
    }

    public void setToParticipantId(Long toParticipantId) {
        this.toParticipantId = toParticipantId;
    }
}

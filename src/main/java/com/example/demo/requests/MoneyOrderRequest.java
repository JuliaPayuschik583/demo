package com.example.demo.requests;

public class MoneyOrderRequest {

    private Long fromAccId;
    private Long toAccId;
    private Long amount;

    public Long getFromAccId() {
        return fromAccId;
    }

    public void setFromAccId(Long fromAccId) {
        this.fromAccId = fromAccId;
    }

    public Long getToAccId() {
        return toAccId;
    }

    public void setToAccId(Long toAccId) {
        this.toAccId = toAccId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}

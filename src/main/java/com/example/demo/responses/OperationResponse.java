package com.example.demo.responses;

import com.example.demo.db.bean.Operation;

import java.util.ArrayList;
import java.util.List;

public class OperationResponse extends Response {

    private List<Operation> operationList;

    public OperationResponse() {
        this.operationList = new ArrayList<>();
    }

    public List<Operation> getOperationList() {
        return operationList;
    }
}

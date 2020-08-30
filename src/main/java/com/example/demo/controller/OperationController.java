package com.example.demo.controller;

import com.example.demo.TransactionStatus;
import com.example.demo.db.OperationRepository;
import com.example.demo.db.bean.Operation;
import com.example.demo.requests.OperationRequest;
import com.example.demo.responses.OperationResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    OperationRepository operationRepository;

    @RequestMapping(value = "/getAllOperationsByAccId", method = RequestMethod.POST)
    public OperationResponse getAllOperationsByAccId(@RequestBody OperationRequest request) {
        OperationResponse response = new OperationResponse();
        try {
            List<Operation> operationList = operationRepository.getAllOperationsByAccId(request.getParticipantId(), request.getAccountId());
            response.getOperationList().addAll(operationList);
            response.setStatus(TransactionStatus.SUCCESSFUL.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(TransactionStatus.ERROR.getStatus());
            response.setMessage(Strings.isNotEmpty(e.getMessage()) ? e.getMessage() : "was Exception");
        }
        return response;
    }
}

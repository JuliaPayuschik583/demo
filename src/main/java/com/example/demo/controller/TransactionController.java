package com.example.demo.controller;

import com.example.demo.db.TransactionRepository;
import com.example.demo.db.bean.Transaction;
import com.example.demo.requests.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactional")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/getAllTransaction", method = RequestMethod.GET)
    public List<Transaction> getAllOperationsByAccId() {
        return transactionRepository.getAllTransaction();
    }

    @RequestMapping(value = "/getAllTransactionByPartId", method = RequestMethod.POST)
    public List<Transaction> getAllTransactionByPartId(@RequestBody TransactionRequest request) {
        return transactionRepository.getAllTransactionByPartId(request.getParticipantId());
    }
}

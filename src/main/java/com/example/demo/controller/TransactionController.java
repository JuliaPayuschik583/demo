package com.example.demo.controller;

import com.example.demo.db.TransactionRepository;
import com.example.demo.db.bean.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<Transaction> transactionList = transactionRepository.getAllTransaction();
        return transactionList;
    }
}

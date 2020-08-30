package com.example.demo.controller;

import com.example.demo.TransactionStatus;
import com.example.demo.Validator;
import com.example.demo.db.AccountRepository;
import com.example.demo.db.bean.Account;
import com.example.demo.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/getAll")
    public List<Account> getAll() {
        return accountRepository.getAll();
    }

    @RequestMapping("/get")
    public Account getById(@RequestParam("accountId") Long accountId) {
        return accountRepository.getById(accountId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response addAccountToParticipant(@RequestBody Account account) {
        Response response = new Response();
        try {
            Validator.isNotNull(account.getParticipantId(), "participantId element required");
            Validator.isNotNull(account.getAmount(), "amount element required");
            Validator.isNotNull(account.getCurrency(), "currency element required");

            accountRepository.addAccount(account);
            response.setStatus(TransactionStatus.SUCCESSFUL.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(TransactionStatus.ERROR.getStatus());
            response.setMessage("was Exception");
        }
        return response;
    }
}

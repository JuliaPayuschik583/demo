package com.example.demo.controller;

import com.example.demo.Validator;
import com.example.demo.db.AccountRepository;
import com.example.demo.db.bean.Account;
import com.example.demo.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
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

    @RequestMapping("/add")
    public void add(@RequestParam("amount") Long amount, @RequestParam("currency") String currency) throws SQLException {
        Account account = new Account();
        account.setAmount(amount);
        account.setCurrency(currency);
        accountRepository.insert(account);
    }

    @RequestMapping(value = "/addaccount", method = RequestMethod.POST)
    public Response addAccountToParticipant(@RequestBody Account account) {
        Response response = new Response();
        try {
            Validator.isNotNull(account.getParticipantId(), "participantId element required");
            Validator.isNotNull(account.getAmount(), "amount element required");
            Validator.isNotNull(account.getCurrency(), "currency element required");

            accountRepository.addAccount(account);
            response.setStatus(1);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(2);
            response.setMessage("was Exception");
        }
        return response;
    }
}

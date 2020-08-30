package com.example.demo.controller;

import com.example.demo.Validator;
import com.example.demo.db.AccountRepository;
import com.example.demo.db.ParticipantRepository;
import com.example.demo.db.bean.Account;
import com.example.demo.db.bean.Participant;
import com.example.demo.requests.ExternalMoneyOrderRequest;
import com.example.demo.requests.InnerMoneyOrderRequest;
import com.example.demo.responses.Response;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    ParticipantRepository participantRepository;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response addParticipant(@RequestBody Participant participant) {
        Response response = new Response();
        try {
            participantRepository.insert(participant);
            response.setStatus(1);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(2);
            response.setMessage("was Exception");
        }
        return response;
    }

    @RequestMapping("/getall")
    public List<Participant> getAll() {
        return participantRepository.getAll();
    }

    @RequestMapping(value = "/innermoneyorder", method = RequestMethod.POST)
    public Response innerMoneyOrder(@RequestBody InnerMoneyOrderRequest request) {
        Response response = new Response();
        try {
            Validator.isNotNull(request.getParticipantId(), "participantId element required");
            Validator.isNotNull(request.getFromAccId(), "fromAccId element required");
            Validator.isNotNull(request.getToAccId(), "toAccId element required");
            Validator.isNotNull(request.getAmount(), "amount element required");

            participantRepository.sendMoney(request.getParticipantId(), request.getParticipantId(),
                    request.getFromAccId(), request.getToAccId(), request.getAmount());
            response.setStatus(1);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(2);
            response.setMessage(Strings.isNotEmpty(e.getMessage()) ? e.getMessage() : "was Exception");
        }
        return response;
    }

    @RequestMapping(value = "/externalMoneyOrder", method = RequestMethod.POST)
    public Response externalMoneyOrder(@RequestBody ExternalMoneyOrderRequest request) {
        Response response = new Response();
        try {
            Validator.isNotNull(request.getFromParticipantId(), "fromParticipantId element required");
            Validator.isNotNull(request.getToParticipantId(), "toParticipantId element required");
            Validator.isNotNull(request.getFromAccId(), "fromAccId element required");
            Validator.isNotNull(request.getToAccId(), "toAccId element required");
            Validator.isNotNull(request.getAmount(), "amount element required");

            participantRepository.sendMoney(request.getFromParticipantId(), request.getToParticipantId(),
                    request.getFromAccId(), request.getToAccId(), request.getAmount());
            response.setStatus(1);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(2);
            response.setMessage(Strings.isNotEmpty(e.getMessage()) ? e.getMessage() : "was Exception");
        }
        return response;
    }
}

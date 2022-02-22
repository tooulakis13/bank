package com.example.bank.resources.rest.controllers;

import com.example.bank.exceptions.BadRequestException;
import com.example.bank.exceptions.InternalServerErrorException;
import com.example.bank.resources.rest.models.errors.ErrorResponses;
import com.example.bank.resources.rest.models.requests.DepositRequest;
import com.example.bank.resources.rest.models.responses.DepositResponse;
import com.example.bank.resources.rest.models.requests.WithdrawRequest;
import com.example.bank.resources.rest.models.responses.WithdrawResponse;
import com.example.bank.resources.rest.services.DepositTransactionService;
import com.example.bank.resources.rest.services.WithdrawTransactionService;
import com.example.bank.resources.rest.threads.WithdrawThread;
import com.example.bank.resources.rest.utils.BankUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class BankApiRes {

    @Autowired
    DepositTransactionService depositTransactionService;

    @Autowired
    WithdrawTransactionService withdrawTransactionService;

    @Autowired
    BankUtils bankUtils;

    private final Logger logger = Logger.getLogger(BankApiRes.class.getName());

    @RequestMapping(value = "/transaction/deposit",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<DepositResponse> depositTransaction(@RequestBody DepositRequest depositRequest) {
        DepositResponse depositResponse = new DepositResponse();
        bankUtils.validateRequest(depositRequest);
        logger.info(String.format("Valid Deposit Request received -> ", depositRequest));
        bankUtils.amountValidation(depositRequest.getTransactionAmount(), ErrorResponses.INVALID_AMOUNT);
        depositResponse = depositTransactionService.execute(depositRequest);

        return new ResponseEntity<>(depositResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/transaction/withdraw",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<WithdrawResponse> withdrawTransaction(@RequestBody WithdrawRequest withdrawRequest) throws InterruptedException {
        WithdrawResponse withdrawResponse = new WithdrawResponse();
        bankUtils.validateRequest(withdrawRequest);
        logger.info(String.format("Valid Withdraw Request received -> ", withdrawRequest));
        bankUtils.amountValidation(withdrawRequest.getTransactionAmount(), ErrorResponses.INVALID_AMOUNT);

        WithdrawThread withdrawThread = new WithdrawThread(withdrawTransactionService, withdrawRequest);
        withdrawThread.start();
        try {
            withdrawThread.join();
            withdrawResponse = withdrawThread.getValue();
            logger.info(String.format("Thread executed successfully -> ", withdrawResponse));
        } catch (Exception e) {
            if (e instanceof BadRequestException || e instanceof InternalServerErrorException) {
                throw e;
            } else if (e instanceof InterruptedException) {
                throw new BadRequestException(ErrorResponses.THREAD_FAILURE, e);
            }
        }

        return new ResponseEntity<>(withdrawResponse, HttpStatus.OK);
    }

}

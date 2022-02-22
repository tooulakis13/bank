package com.example.bank.resources.rest.threads;

import com.example.bank.exceptions.BadRequestException;
import com.example.bank.exceptions.InternalServerErrorException;
import com.example.bank.resources.rest.models.requests.WithdrawRequest;
import com.example.bank.resources.rest.models.responses.WithdrawResponse;
import com.example.bank.resources.rest.services.WithdrawTransactionService;

public class WithdrawThread extends Thread {

    WithdrawTransactionService withdrawTransactionService;

    WithdrawRequest withdrawRequest;

    private volatile WithdrawResponse withdrawResponse;

    private volatile BadRequestException badRequestException;
    private volatile InternalServerErrorException internalServerErrorException;

    public WithdrawThread(WithdrawTransactionService withdrawTransactionService, WithdrawRequest withdrawRequest)
    {
        this.withdrawTransactionService = withdrawTransactionService;
        this.withdrawRequest = withdrawRequest;
    }

    public void run() {
        try{
            withdrawResponse = withdrawTransactionService.execute(withdrawRequest);
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                badRequestException = (BadRequestException) e;
            } else if (e instanceof InternalServerErrorException) {
                internalServerErrorException = (InternalServerErrorException) e;
            }
        }
    }

    public WithdrawResponse getValue() {
        if (badRequestException != null) {
            throw badRequestException;
        } else if (internalServerErrorException != null) {
            throw internalServerErrorException;
        }
        return withdrawResponse;
    }
}

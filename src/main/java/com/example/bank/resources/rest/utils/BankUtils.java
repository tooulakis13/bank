package com.example.bank.resources.rest.utils;

import com.example.bank.dto.AccountMongoDTO;
import com.example.bank.dto.TransactionAuditMongoDTO;
import com.example.bank.exceptions.BadRequestException;
import com.example.bank.resources.rest.models.Transaction;
import com.example.bank.resources.rest.models.enums.TransactionType;
import com.example.bank.resources.rest.models.errors.ErrorResponses;
import com.example.bank.resources.rest.models.requests.DepositRequest;
import com.example.bank.resources.rest.models.requests.WithdrawRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class BankUtils {

    public void validateRequest(Object object) {
        if (object.getClass().getName().equals(DepositRequest.class.getName())){
            DepositRequest depositRequest = (DepositRequest) object;
            if (depositRequest.getFan() == null || depositRequest.getTransactionAmount() ==null) {
                throw new BadRequestException(ErrorResponses.INVALID_REQUEST);
            }
        } else {
            WithdrawRequest withdrawRequest = (WithdrawRequest) object;
            if (withdrawRequest.getFan() == null || withdrawRequest.getTransactionAmount() ==null) {
                throw new BadRequestException(ErrorResponses.INVALID_REQUEST);
            }
        }
    }

    public void amountValidation (Double amount, String errorResponseMessage) {

        if (amount > 0) {
            return;
        } else {
            throw new BadRequestException(errorResponseMessage);
        }
    }

    public Double roundDouble (Double value, int decimalpoint) {
        value = value * Math.pow(10, decimalpoint);
        value = Math.floor(value);
        value = value / Math.pow(10, decimalpoint);

        return value;
    }

    public AccountMongoDTO addNewAccount(Transaction transaction) {

        AccountMongoDTO accountMongoDTO = new AccountMongoDTO();

        accountMongoDTO.setFan(transaction.getAccountId());
        accountMongoDTO.setPan(transaction.getAccountId());
        accountMongoDTO.setTotalAmount(transaction.getAmount());
        return accountMongoDTO;
    }

    public AccountMongoDTO calculateTotalAdditionNewAccount(Transaction transaction) {

        AccountMongoDTO newAccountMongoDTO = addNewAccount(transaction);

        Double totalAmount = newAccountMongoDTO.getTotalAmount();
        Double newTotalAmount = totalAmount + transaction.getAmount();
        newAccountMongoDTO.setTotalAmount(roundDouble(newTotalAmount, 2));

        return newAccountMongoDTO;

    }

    public AccountMongoDTO calculateTotalAddition(AccountMongoDTO record, Transaction transaction) {

        Double totalAmount = record.getTotalAmount();
        Double newTotalAmount = totalAmount + transaction.getAmount();
        record.setTotalAmount(roundDouble(newTotalAmount, 2));

        return record;

    }

    public void calculateTotalSubtract(AccountMongoDTO record, Transaction transaction) {

        Double totalAmount = record.getTotalAmount();
        Double newTotalAmount = totalAmount - transaction.getAmount();
        amountValidation(newTotalAmount, ErrorResponses.INSUFFICIENT_FUNDS);
        record.setTotalAmount(roundDouble(newTotalAmount, 2));

    }

    public void populateTransactionAuditMongoDTO(TransactionAuditMongoDTO transactionAuditMongoDTO, AccountMongoDTO accountMongoDTO, Transaction transaction) {

        transactionAuditMongoDTO.setPan(accountMongoDTO.getFan());
        transactionAuditMongoDTO.setTransactionType(transaction.getTransactionType());
        transactionAuditMongoDTO.setTransactionAmount(transaction.getAmount());
        transactionAuditMongoDTO.setTotalAmount(accountMongoDTO.getTotalAmount());
        transactionAuditMongoDTO.setTimestamp(transaction.getTimestamp());

    }
}

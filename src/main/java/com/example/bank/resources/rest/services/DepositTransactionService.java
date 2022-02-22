package com.example.bank.resources.rest.services;

import com.example.bank.dao.AccountMongoRepository;
import com.example.bank.dto.AccountMongoDTO;
import com.example.bank.dto.TransactionAuditMongoDTO;
import com.example.bank.exceptions.BadRequestException;
import com.example.bank.exceptions.InternalServerErrorException;
import com.example.bank.kafka.producers.Producer;
import com.example.bank.resources.rest.models.Transaction;
import com.example.bank.resources.rest.mappers.DepositRequestToTransactionMapper;
import com.example.bank.resources.rest.models.enums.TransactionType;
import com.example.bank.resources.rest.models.errors.ErrorResponses;
import com.example.bank.resources.rest.models.requests.DepositRequest;
import com.example.bank.resources.rest.models.responses.DepositResponse;
import com.example.bank.resources.rest.utils.BankUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Slf4j
public class DepositTransactionService {

    @Autowired
    DepositRequestToTransactionMapper depositRequestToTransactionMapper;

    @Autowired
    AccountMongoRepository accountMongoRepository;

    @Autowired
    BankUtils bankUtils;

    @Autowired
    Producer producer;

    @Autowired
    Gson gson;

    private static final Logger logger = Logger.getLogger(DepositTransactionService.class.getName());

    public DepositResponse execute(DepositRequest depositRequest) {

        Transaction transaction = depositRequestToTransactionMapper.map(depositRequest);
        logger.info(String.format("Mapped deposit request to Transaction object -> ", transaction));
        return depositTransactionImplementation(transaction);

    }

    private DepositResponse depositTransactionImplementation (Transaction transaction) {

        DepositResponse depositResponse = new DepositResponse();

        AccountMongoDTO accountMongoDTO;

        TransactionAuditMongoDTO transactionAuditMongoDTO = new TransactionAuditMongoDTO();

        Optional<AccountMongoDTO> accountDetails = accountMongoRepository.findByFan(transaction.getAccountId());

        if (accountDetails.isPresent()) {
            transactionAuditMongoDTO.setFan(accountDetails.get().getFan());
            if (accountDetails.get().getPan().longValue() != transaction.getAccountId().longValue()) {
                accountDetails = accountMongoRepository.findByFan(accountDetails.get().getPan());
                if (accountDetails.isPresent()) {
                    accountMongoDTO = bankUtils.calculateTotalAddition(accountDetails.get(), transaction);
                } else {
                    throw new BadRequestException(ErrorResponses.JOINT_ACCOUNTS_MISMATCH);
                }
            } else {
                accountMongoDTO = bankUtils.calculateTotalAddition(accountDetails.get(), transaction);
            }
        } else {
            accountMongoDTO = bankUtils.calculateTotalAdditionNewAccount(transaction);
        }

        try {
            accountMongoRepository.save(accountMongoDTO);
            depositResponse.setTotalAmount(accountMongoDTO.getTotalAmount());

            bankUtils.populateTransactionAuditMongoDTO(transactionAuditMongoDTO, accountMongoDTO, transaction);
            logger.info(String.format("Populated TransactionAuditMongoDTO object -> ", transactionAuditMongoDTO));

            String transactionMessage = gson.toJson(transactionAuditMongoDTO);

            producer.sendMessage(transactionMessage);
        } catch (Exception e) {
            throw new InternalServerErrorException(ErrorResponses.SAVE_FAILURE, e);
        }

        return depositResponse;
    }

}

package com.example.bank.resources.rest.services;

import com.example.bank.dao.AccountMongoRepository;
import com.example.bank.dto.AccountMongoDTO;
import com.example.bank.dto.TransactionAuditMongoDTO;
import com.example.bank.exceptions.BadRequestException;
import com.example.bank.exceptions.InternalServerErrorException;
import com.example.bank.kafka.producers.Producer;
import com.example.bank.resources.rest.mappers.WithdrawRequestToTransactionMapper;
import com.example.bank.resources.rest.models.Transaction;
import com.example.bank.resources.rest.models.errors.ErrorResponses;
import com.example.bank.resources.rest.models.requests.WithdrawRequest;
import com.example.bank.resources.rest.models.responses.WithdrawResponse;
import com.example.bank.resources.rest.utils.BankUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@Slf4j
public class WithdrawTransactionService {

    @Autowired
    WithdrawRequestToTransactionMapper withdrawRequestToTransactionMapper;

    @Autowired
    AccountMongoRepository accountMongoRepository;

    @Autowired
    BankUtils bankUtils;

    @Autowired
    Producer producer;

    @Autowired
    Gson gson;

    private static final Logger logger = Logger.getLogger(WithdrawTransactionService.class.getName());

    public synchronized WithdrawResponse execute(WithdrawRequest withdrawRequest) {

        Transaction transaction = withdrawRequestToTransactionMapper.map(withdrawRequest);
        logger.info(String.format("Mapped withdraw request to Transaction object -> ", transaction));
        return withdrawTransactionImplementation(transaction);

    }

    private WithdrawResponse withdrawTransactionImplementation(Transaction transaction) {

        WithdrawResponse withdrawResponse = new WithdrawResponse();

        TransactionAuditMongoDTO transactionAuditMongoDTO = new TransactionAuditMongoDTO();

        Optional<AccountMongoDTO> accountDetails = accountMongoRepository.findByFan(transaction.getAccountId());

        if (accountDetails.isPresent()) {
            transactionAuditMongoDTO.setFan(accountDetails.get().getFan());
            if (accountDetails.get().getPan() != transaction.getAccountId()) {
                accountDetails = accountMongoRepository.findByFan(accountDetails.get().getPan());
                if (accountDetails.isPresent()) {
                    bankUtils.calculateTotalSubtract(accountDetails.get(), transaction);
                } else {
                    throw new BadRequestException(ErrorResponses.JOINT_ACCOUNTS_MISMATCH);
                }
            } else {
                bankUtils.calculateTotalSubtract(accountDetails.get(), transaction);
            }
        } else {
            throw new BadRequestException(ErrorResponses.NO_SUCH_ACCOUNT);
        }

        try {
            accountMongoRepository.save(accountDetails.get());
            withdrawResponse.setTotalAmount(accountDetails.get().getTotalAmount());

            bankUtils.populateTransactionAuditMongoDTO(transactionAuditMongoDTO, accountDetails.get(), transaction);
            logger.info(String.format("Populated TransactionAuditMongoDTO object -> ", transactionAuditMongoDTO));

            String transactionMessage = gson.toJson(transactionAuditMongoDTO);

            producer.sendMessage(transactionMessage);
        } catch (Exception e) {
            throw new InternalServerErrorException(ErrorResponses.SAVE_FAILURE, e);
        }

        return withdrawResponse;
    }

}

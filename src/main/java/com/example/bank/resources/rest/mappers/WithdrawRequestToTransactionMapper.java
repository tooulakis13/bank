package com.example.bank.resources.rest.mappers;

import com.example.bank.resources.rest.models.Transaction;
import com.example.bank.resources.rest.models.enums.TransactionType;
import com.example.bank.resources.rest.models.requests.WithdrawRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WithdrawRequestToTransactionMapper {

    public Transaction map(WithdrawRequest src) {

        Transaction dst = new Transaction();

        dst.setTransactionType(TransactionType.WITHDRAW);
        dst.setAmount(src.getTransactionAmount());
        dst.setAccountId(src.getFan());
        dst.setTimestamp(System.currentTimeMillis());

        return dst;

    }
}

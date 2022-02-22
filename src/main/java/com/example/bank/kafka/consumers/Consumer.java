package com.example.bank.kafka.consumers;

import com.example.bank.dao.TransactionAuditMongoRepository;
import com.example.bank.dto.TransactionAuditMongoDTO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class Consumer {

    private final Logger logger = Logger.getLogger(Consumer.class.getName());

    @Autowired
    TransactionAuditMongoRepository transactionAuditMongoRepository;

    Gson gson = new Gson();

    @KafkaListener(topics = "bank", groupId = "transactions")
    public void consume(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));

        TransactionAuditMongoDTO transactionAuditMongoDTO = gson.fromJson(message, TransactionAuditMongoDTO.class);

        transactionAuditMongoRepository.save(transactionAuditMongoDTO);
    }
}

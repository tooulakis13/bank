package com.example.bank.dao;

import com.example.bank.dto.AccountMongoDTO;
import com.example.bank.dto.TransactionAuditMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionAuditMongoRepository extends MongoRepository<TransactionAuditMongoDTO, String> {

}

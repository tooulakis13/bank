package com.example.bank.dao;

import com.example.bank.dto.AccountMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountMongoRepository extends MongoRepository<AccountMongoDTO, String> {

    Optional<AccountMongoDTO> findByFan(Long fan);
}

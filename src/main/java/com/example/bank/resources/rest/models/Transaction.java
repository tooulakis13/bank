package com.example.bank.resources.rest.models;

import com.example.bank.resources.rest.models.enums.TransactionType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class Transaction
{
	private String id;

	private TransactionType transactionType;

	private Double amount;

	private Long accountId;

	private Long timestamp;

}
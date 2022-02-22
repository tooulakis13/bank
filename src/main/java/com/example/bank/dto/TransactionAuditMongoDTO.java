package com.example.bank.dto;

import com.example.bank.resources.rest.models.enums.TransactionType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "transactionAudit")
public class TransactionAuditMongoDTO
{
	@Id
	private String _id;

	private Long fan;

	private Long pan;

	private Double transactionAmount;

	private Double totalAmount;

	private TransactionType transactionType;

	private Long timestamp;

}
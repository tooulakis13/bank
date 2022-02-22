package com.example.bank.dto;

import com.example.bank.resources.rest.models.enums.AccountStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "accounts")
public class AccountMongoDTO
{
	@Id
	private String _id;

	private Long fan;

	private Long pan;

	private Double totalAmount;

}
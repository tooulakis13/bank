package com.example.bank.resources.rest.models.requests;

import java.io.Serializable;

public class DepositRequest implements Serializable {

        private Long fan = null;

        private Double transactionAmount = null;

        public DepositRequest() {
        }

        public DepositRequest(Long fan, Double transactionAmount) {
                this.fan = fan;
                this.transactionAmount = transactionAmount;
        }

        public Long getFan() {
                return fan;
        }

        public Double getTransactionAmount() {
                return transactionAmount;
        }

        public void setFan(Long fan)
        {
                this.fan = fan;
        }

        public void setTransactionAmount(Double transactionAmount)
        {
                this.transactionAmount = transactionAmount;
        }
}

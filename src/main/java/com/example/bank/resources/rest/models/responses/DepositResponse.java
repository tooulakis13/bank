package com.example.bank.resources.rest.models.responses;

import java.io.Serializable;

public class DepositResponse implements Serializable {

        private Double totalAmount = null;

        public DepositResponse() { }

        public DepositResponse(Double totalAmount) {
                this.totalAmount = totalAmount;
        }

        public Double getTotalAmount() {
                return totalAmount;
        }

        public void setTotalAmount(Double totalAmount)
        {
                this.totalAmount = totalAmount;
        }
}

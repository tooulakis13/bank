package com.example.bank.resources.rest.models.responses;

import java.io.Serializable;

public class WithdrawResponse implements Serializable {

        private Double totalAmount = null;

        public WithdrawResponse() { }

        public WithdrawResponse(Double totalAmount) {
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

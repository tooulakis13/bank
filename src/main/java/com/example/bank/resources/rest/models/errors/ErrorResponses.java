package com.example.bank.resources.rest.models.errors;

public final class ErrorResponses {

        private ErrorResponses(){}

        public static final String JOINT_ACCOUNTS_MISMATCH = "Parent account does not exist in DB.";
        public static final String SAVE_FAILURE = "Encountered exception while saving in DB.";
        public static final String INVALID_AMOUNT = "Invalid given amount. Amount value should be greater than 0.";
        public static final String INSUFFICIENT_FUNDS = "Unable to perform withdraw. Insufficient funds.";
        public static final String NO_SUCH_ACCOUNT = "Account not found. Unable to perform withdraw.";
        public static final String INVALID_REQUEST = "Invalid items provided in request payload.";
        public static final String THREAD_FAILURE = "Encountered error while executing action.";

}

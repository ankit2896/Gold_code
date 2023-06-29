package com.freecharge.financial.utility;

public enum GoldErrorCodes {

    IN_ERR_4001("Invalid Parameters"),
    UNABLE_TO_CONNECT_REDIS_SERVER("Unable to connect redis serevr, Kindly check configuration"),
    IN_ERR_500("Something went wrong, Kindly try after some time");


    private String errorMessage;

    private GoldErrorCodes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}

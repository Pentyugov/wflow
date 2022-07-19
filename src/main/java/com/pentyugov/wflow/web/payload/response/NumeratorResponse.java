package com.pentyugov.wflow.web.payload.response;

public class NumeratorResponse {

    public NumeratorResponse() {

    }

    public NumeratorResponse(String number) {
        this.number = number;
    }
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

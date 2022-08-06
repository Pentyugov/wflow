package com.pentyugov.wflow.core.service;

public interface NumeratorService {

    String NAME = "wflow$NumeratorService";

    String TYPE_TASK = "TASK";
    String TYPE_MEETING = "MEETING";

    String ORDER_NEXT = "NEXT";
    String ORDER_LAST = "LAST";

    String getNextNumber(String type);
}

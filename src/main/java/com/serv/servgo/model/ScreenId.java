package com.serv.servgo.model;

public enum ScreenId {
    WELCOME("Welcome"),
    CHECK_IN("CheckIn"),
    SERVICE_SELECTION("ServiceSelection"),
    QUEUE("queue"),
    ALERT("alter"),
    MONITOR("monitor"),
    PAYMENT("payment"),
    RECEIPT("receipt"),
    MAPS("maps");

    private final String id;

    ScreenId(String id) {
        this.id = id;
    }
    public String id() {
        return id;
    }
}
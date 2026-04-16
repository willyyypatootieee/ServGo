package com.serv.servgo.model;


public enum ServiceType {
    CHARGING("Mengisi", 75000),
    LIGHT_SERVICE("Service Ringan", 45000);

    private final String label;
    private final int baseCost;

    ServiceType(String label, int baseCost) {
        this.label = label;
        this.baseCost = baseCost;
    }

    public String label() {
        return label;

    }

    public int baseCost() {
        return baseCost;
    }
}
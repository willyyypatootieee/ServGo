package com.serv.servgo.model;

public class SessionData {
    private String plateNumber;
    private ServiceType serviceType;
    private int queueNumber;
    private int estimatedWaitMinutes;
    private PaymentMethod paymentMethod;
    private int recommendedPercent = 80;
    private int chargingSlotsAvailable;
    private int progressPercent;

    public void reset() {
        plateNumber = null;
        serviceType = null;
        queueNumber = 0;
        estimatedWaitMinutes = 0;
        paymentMethod = null;
        recommendedPercent = 80;
        chargingSlotsAvailable = 0;
        progressPercent = 0;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public int getEstimatedWaitMinutes() {
        return estimatedWaitMinutes;
    }

    public void setEstimatedWaitMinutes(int estimatedWaitMinutes) {
        this.estimatedWaitMinutes = estimatedWaitMinutes;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getRecommendedPercent() {
        return recommendedPercent;
    }

    public void setRecommendedPercent(int recommendedPercent) {
        this.recommendedPercent = recommendedPercent;
    }

    public int getChargingSlotsAvailable() {
        return chargingSlotsAvailable;
    }

    public void setChargingSlotsAvailable(int chargingSlotsAvailable) {
        this.chargingSlotsAvailable = chargingSlotsAvailable;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public int totalCost() {
        return serviceType == null ? 0 : serviceType.baseCost();
    }
}
package com.serv.servgo.service;

import com.serv.servgo.model.ServiceType;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueService {
    private final AtomicInteger chargingQueue = new AtomicInteger(100);
    private final AtomicInteger lightServiceQueue = new AtomicInteger(200);
    private final Random random = new Random();
    private boolean demoSeeded;

    public void seedDemoQueues() {
        if (demoSeeded) {
            return;
        }

        chargingQueue.set(ThreadLocalRandom.current().nextInt(310, 420));
        lightServiceQueue.set(ThreadLocalRandom.current().nextInt(120, 250));
        demoSeeded = true;
    }

    public int nextQueueNumber(ServiceType type) {
        if (type == ServiceType.CHARGING) {
            return chargingQueue.incrementAndGet();
        }
        return lightServiceQueue.incrementAndGet();
    }

    public int estimateWaitMinutes() {
        int peopleAhead = ThreadLocalRandom.current().nextInt(1, 5);
        return peopleAhead * 5;
    }

    public int availableChargingSlots() {
        return ThreadLocalRandom.current().nextInt(1, 5);
    }
}

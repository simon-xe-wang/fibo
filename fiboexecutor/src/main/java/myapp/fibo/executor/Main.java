/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    private static void startTaskConsumers() {
        ExecutorService executorService = Executors.newFixedThreadPool(1); // Just one thread for now
        executorService.submit(new FiboTaskConsumerKafka());
        log.info("Fibo Task Consumer started successfully.");
    }

    public static void main(String[] args) {
        startTaskConsumers();
    }
}

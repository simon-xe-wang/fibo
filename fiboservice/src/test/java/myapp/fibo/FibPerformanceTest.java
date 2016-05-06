/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo;

import myapp.util.TestUtil;
import org.junit.Test;
import myapp.util.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * To collect some performance data in single request
 */
public class FibPerformanceTest {

    @Test
    public void testFiboPerfMultiThread() throws Exception {
        int threadCount = 4;
        int fiboSN = 10000;
        List<Thread> workers = new ArrayList<>();

        for (int i = 0; i < threadCount; i ++) {
            String workerName = "worker" + i;
            Thread worker = new Thread(new FiboClientWorker(fiboSN, workerName));
            worker.start();
            workers.add(worker);
        }

        for (int i = 0; i < threadCount; i++) {
            workers.get(0).join();
        }
    }

    private static class FiboClientWorker implements Runnable {
        private final String name;
        private int count;


        public FiboClientWorker(int count, String name) {
            this.count = count;
            this.name = name;
            Thread.currentThread().setName(name);
        }

        @Override
        public void run() {
            System.out.println("Worker " + this.name + " is running ...");
            String resultFile = "./results/fib_perf_" + Thread.currentThread().getName();
            Timer timer = new Timer();
            try {
           //     TestUtil.loadFibIntoFile(count, resultFile);
            } catch (Exception e) {
                throw new RuntimeException("Fail to test " + e);
            }
            System.out.println("Done. Time spent: " + timer.end());
        }
    }
}

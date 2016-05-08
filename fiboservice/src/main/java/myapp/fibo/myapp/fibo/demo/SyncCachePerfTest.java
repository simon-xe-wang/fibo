package myapp.fibo.myapp.fibo.demo;

import myapp.fibo.client.FiboClient;

import java.util.ArrayList;
import java.util.List;

public class SyncCachePerfTest {
    private static class Worker implements Runnable {
        int sn;

        public Worker(int sn) {
            this.sn = sn;
        }

        public void run() {
            FiboClient client = new FiboClient();
            Timer timer = new Timer();
            try {
                client.getFiboSyncCache(sn);
            } catch (Exception e) {
                throw new RuntimeException("Error", e);
            }

            System.out.println("Spent " + timer.end());
            //    System.out.println("value is " + fn.toString());
            //    System.out.println("value size is " + fn.toString().length());
        }
    }

    public static void main(String[] args) throws Exception {
        int count = Integer.parseInt(args[1]);
        int sn = Integer.parseInt(args[0]);

        List<Thread> threadList = new ArrayList<Thread>();

        for (int i=0; i<count; i++) {
            Worker worker = new Worker(sn);
            Thread t = new Thread(worker);
            t.start();
            threadList.add(t);
        }

        for (Thread t : threadList) {
            t.join();
        }
    }
}


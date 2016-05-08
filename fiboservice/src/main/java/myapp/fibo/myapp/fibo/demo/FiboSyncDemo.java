/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.myapp.fibo.demo;

import myapp.fibo.client.FiboClient;

public class FiboSyncDemo {
    public static void main(String[] args) throws Exception {
        FiboClient client = new FiboClient();
        client.getFiboSync(100);
    }
}

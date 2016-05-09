/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.myapp.fibo.demo;

import myapp.fibo.client.FiboClient;

public class FiboAyncDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("Usage: get_fibo.sh sn pathToResultFile");
        System.out.println("Example: get_fibo.sh 100 ./result");
        
        if (args.length < 2) {
            System.exit(1);
        }

        int sn = Integer.parseInt(args[0]);
        String file = args[1];
        FiboClient client = new FiboClient();
        client.getFiboAsyncAsFile(sn, file);
    }
}

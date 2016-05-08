/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo;

import java.math.BigInteger;

public class FiboSequenceIterator {

    private FiboValueHandler fiboHandler;
    private int sn;

    public FiboSequenceIterator(int sn) {
        this.sn = sn;
    }

    public void run() {
        BigInteger fn_2 = BigInteger.ZERO;
        BigInteger fn_1 = BigInteger.ONE;
        BigInteger fn;

        if (sn < 0) {
            throw new IllegalArgumentException("Invalid negative number");
        }

        if (sn >= 1) fiboHandler.handle(1, BigInteger.ZERO);
        if (sn >= 2) fiboHandler.handle(2, BigInteger.ONE);
        for (int i = 3; i <= sn; i++) {
            fn = fn_2.add(fn_1);
            fiboHandler.handle(i, fn);
            // shift forward
            fn_2 = fn_1;
            fn_1 = fn;
        }
    }

    public void setHandler(FiboValueHandler handler) {
        this.fiboHandler = handler;
    }
}

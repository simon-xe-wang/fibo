/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo;

import myapp.fibo.seqstore.FiboSequenceStore;
import myapp.fibo.seqstore.FiboSequenceStoreRedis;

import java.math.BigInteger;

public class FiboSequenceCacheIterator {

    private FiboValueHandler fiboHandler;
    private int sn;
    FiboSequenceStore fiboStore = new FiboSequenceStoreRedis();

    public FiboSequenceCacheIterator(int sn) {
        this.sn = sn;
    }

    public void run() {
        for (int i = 1; i <= sn; i++) {
            String fiboVal = fiboStore.getFiboValStr(i);
            fiboHandler.handle(i, fiboVal);
        }
    }

    public void setHandler(FiboValueHandler handler) {
        this.fiboHandler = handler;
    }
}

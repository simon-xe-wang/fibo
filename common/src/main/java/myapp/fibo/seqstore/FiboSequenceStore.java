/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.seqstore;

import java.math.BigInteger;

/**
 * Store containing all Fibonacci values
 */
public interface FiboSequenceStore {

    /**
     * Get the max value cached in store. That means all smaller values are already cached.
     * @return
     */
    int getMaxCachedSn();

    /**
     * Write a Fibonacci value to store with sn as key.
     * @param sn
     * @param val
     */
    void writeFiboValue(int sn, BigInteger val);

    /**
     * Get a value from store by sn
     * @param sn
     * @return
     */
    BigInteger getFiboVal(int sn);

    /**
     * Try to update the max cached sn in store. If sn < cachedSn, do nothing
     * @param sn
     */
    void tryUpdateMaxCachedSn(int sn);

    /**
     * Close the store.
     */
    void close();
}

/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo;

import java.math.BigInteger;

public interface FiboValueHandler {
    void handle(int sn, BigInteger val);
    void handle(int sn, String val);

    void flush();
}

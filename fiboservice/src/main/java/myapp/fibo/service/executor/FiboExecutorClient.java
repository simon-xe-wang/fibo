/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.service.executor;

import myapp.fibo.FiboTask;

public interface FiboExecutorClient {

    void submit(FiboTask task) throws Exception;
}

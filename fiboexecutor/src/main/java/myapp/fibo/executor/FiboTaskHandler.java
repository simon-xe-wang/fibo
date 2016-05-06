/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.executor;

import myapp.fibo.FiboTask;

public interface FiboTaskHandler {
    /**
     * Handle each task.
     * @param task
     * @throws Exception
     */
    void handle(FiboTask task) throws Exception;
}

/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.service.executor;

import myapp.fibo.service.BeanFactory;
import org.junit.After;
import org.junit.Before;

public class FiboTaskQueueMemImplTest {

    FiboTaskQueue taskQueue = BeanFactory.getInstance().getTaskQueue();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}

package myapp.fibo.taskstore;

import junit.framework.Assert;
import myapp.fibo.FiboTask;
import myapp.fibo.testutil.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FiboTaskStoreRedisTest {

    FiboTaskStoreRedis taskStore = new FiboTaskStoreRedis();

    @Before
    public void setup() {

    }

    @After
    public void teardown() {

    }

    @Test
    public void testSaveAndQuery() {
        FiboTask task = TestUtil.createTask(FiboTask.STATE_READY, 5);
        taskStore.save(task);

        FiboTask dstTask = taskStore.query(task.getId());
        Assert.assertEquals(task, dstTask);
    }
}
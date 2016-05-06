package myapp.fibo.service;

import myapp.fibo.IdGenerator;
import myapp.fibo.IdGeneratorTimeImpl;
import myapp.fibo.service.executor.FiboExecutorClient;
import myapp.fibo.service.executor.FiboExecutorClientKafka;
import myapp.fibo.taskstore.FiboTaskStore;

/**
 * The factory to get the implementation of interfaces. Could be replaced with Spring.
 */
public class BeanFactory {

    private static BeanFactory inst = null;

    private FiboExecutorClient executorClient = null;
    private IdGenerator idGeneratorInst = null;
    private FiboTaskStore taskStore = null;

    public static BeanFactory getInstance() {
        if (inst == null) {
            inst = new BeanFactory();
        }
        return inst;
    }

    public IdGenerator getIdGenerator() {
        if (idGeneratorInst == null) {
            idGeneratorInst = new IdGeneratorTimeImpl();
        }
        return idGeneratorInst;
    }

    public FiboExecutorClient getExecutorClient() {
        if (executorClient == null) {
            executorClient = new FiboExecutorClientKafka();
        }
        return executorClient;
    }
}

package myapp.fibo.taskstore;

import myapp.fibo.FiboTask;

public interface FiboTaskStore {
    void save(FiboTask task);
    FiboTask query(String id);
}

package myapp.fibo.executor;

import myapp.fibo.FiboTask;
import myapp.fibo.taskstore.FiboTaskStore;
import myapp.fibo.taskstore.FiboTaskStoreRedis;

public class FiboTaskHandlerRedis implements FiboTaskHandler {

    private FiboTaskStore taskStore = new FiboTaskStoreRedis();

    @Override
    public void handle(FiboTask task) throws Exception {

        // Check task status in db in case this is resent to consumer.
        if (taskReadyInDB(task)) {
            return;
        }

        FiboSequenceGeneratorRedis fiboGen = new FiboSequenceGeneratorRedis();
        fiboGen.generateAndCache(task.getSn());

        task.setState(FiboTask.STATE_READY);
        taskStore.save(task);
    }

    private boolean taskReadyInDB(FiboTask task) {
        FiboTask dbTask = taskStore.query(task.getId());
        return (dbTask.getState() == FiboTask.STATE_READY);
    }
}

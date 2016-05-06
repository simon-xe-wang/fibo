package myapp.fibo.taskstore;

import myapp.fibo.FiboConfig;
import myapp.fibo.FiboTask;
import myapp.fibo.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class FiboTaskStoreRedis implements FiboTaskStore {
    private static Logger log = LoggerFactory.getLogger(FiboTaskStoreRedis.class);

    @Override
    public void save(FiboTask task) {
        Jedis jedis = getJedis();
        try {
            jedis.set(task.getId(), JsonUtils.taskToJson(task));
        } finally {
            jedis.close();
        }
    }

    @Override
    public FiboTask query(String id) {
        String taskStr = null;
        Jedis jedis = getJedis();
        try {
            taskStr = jedis.get(id);
            return JsonUtils.jsonToTask(taskStr);
        } catch (Exception e) {
            log.warn("Invalid task str in store: {}", taskStr);
            return null;
        } finally {
            jedis.close();
        }
    }

    private Jedis getJedis() {
        return new Jedis(FiboConfig.getInstance().getRedisHost());
    }
}

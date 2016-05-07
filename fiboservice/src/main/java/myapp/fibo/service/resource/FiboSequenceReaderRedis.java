package myapp.fibo.service.resource;

import myapp.fibo.FiboConfig;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;

public class FiboSequenceReaderRedis {

    private Jedis jedis;
    private int cursor = 1;
    private int sn;

    public FiboSequenceReaderRedis(int sn) {
        this.sn = sn;
        jedis = new Jedis(FiboConfig.getInstance().getRedisHost());
    }

    public String read() {
        if (cursor > sn) {
            return null;
        }

        String val = jedis.get(Integer.toString(cursor));
        cursor++;
        return val;
    }

    public void close() throws IOException {
        jedis.close();
    }
}

package myapp.fibo.executor;

import junit.framework.Assert;
import myapp.fibo.FiboConfig;
import myapp.fibo.seqstore.FiboSequenceStoreRedis;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;

public class FiboSequenceGeneratorRedisTest {
    Jedis jedis;

    @Before
    public void setUp() throws Exception {
        jedis = new Jedis(FiboConfig.getInstance().getRedisHost());
        jedis.flushDB();
    }

    @After
    public void tearDown() throws Exception {
        jedis.close();
    }

    /**
     * Generate a fibo sequence and verify if all data stored in cache and max cached sn is updated correctly
     */
    @Test
    public void testGenerateFiboWithoutCache() {
        int sn = 100;

        FiboSequenceGeneratorRedis fiboGen = new FiboSequenceGeneratorRedis();
        fiboGen.generateAndCache(sn);
        verifyFiboFromCache(sn);
    }

    /**
     * Data already in cache, max cached sn should be old SN.
     */
    @Test
    public void testGenerateFiboAlreadyCached() {
        int sn1 = 100;
        FiboSequenceGeneratorRedis fiboGen = new FiboSequenceGeneratorRedis();
        fiboGen.generateAndCache(sn1);

        int sn2 = 50;
        fiboGen = new FiboSequenceGeneratorRedis();
        fiboGen.generateAndCache(sn2);

        verifyFiboFromCache(sn1);
    }

    public void verifyFiboFromCache(int sn) {
        BigInteger fn_2 = BigInteger.ZERO;
        BigInteger fn_1 = BigInteger.ONE;
        BigInteger fn;

        for (int i = 1; i <= sn; i++) {
            String val = jedis.get(new Integer(i).toString());

            if (i == 1) {
                Assert.assertEquals("Error sn = " + i, "0", val);
            } else if (i == 2) {
                Assert.assertEquals("Error sn = " + i, "1", val);
            } else {
                fn = fn_2.add(fn_1);
                Assert.assertEquals("Error sn = " + i, fn.toString(), val);

                fn_2 = fn_1;
                fn_1 = fn;
            }
        }

        String maxCachedSn = jedis.get(FiboSequenceStoreRedis.KEY_MAX_SN_CACHED);
        Assert.assertEquals("Max Cached SN is not equal", sn, Integer.parseInt(maxCachedSn));
    }
}

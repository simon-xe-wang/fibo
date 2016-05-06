package myapp.fibo.service.executor.impl;

import junit.framework.Assert;
import myapp.fibo.FiboConfig;
import myapp.util.TestUtil;
import org.junit.*;
import redis.clients.jedis.Jedis;

import java.math.BigInteger;
import java.util.List;

public class FiboSequenceGeneratorRedisTest {

    final private static List<BigInteger> fiboSeq = TestUtil.getFiboSeq(100);
    private Jedis jedis;

    @Before
    public void setup() {
        jedis = new Jedis(FiboConfig.getInstance().getRedisHost());
        jedis.flushDB();
    }

    @After
    public void tearDown() {
        jedis.flushDB();
        jedis.close();
    }

    /**
     * Suppose Cache is empty.
     * Expect result:  1) Should be able to get values of 45 number in cache and all values should be correct
     *                 2) The maxFiboCached should be 45;
     * @throws Exception
     */
    @Test
    public void testGenerateAndCache() throws Exception {
        int sn = 45;

        FiboSequenceGeneratorRedis fiboGen = new FiboSequenceGeneratorRedis();
        fiboGen.generateAndCache(sn);

        // Verify each value
        for (int i = 3; i <= sn; i++) {
            String strVal = jedis.get(Integer.toString(i));
            Assert.assertNotNull("The value of sn " + i + "should NOT be null.", strVal);
            BigInteger value = new BigInteger(strVal);
            Assert.assertEquals("Fibo value is not expected. Current sn = " + i, fiboSeq.get(i), value);
        }

        // Verify max_sn_cached
        int max_fibo_cached = Integer.parseInt(jedis.get(FiboSequenceGeneratorRedis.KEY_MAX_SN_CACHED));
        Assert.assertEquals("Cached max_fibo_cached is not equal to expected one", sn, max_fibo_cached);
    }

    /**
     * The fibo values of 3-40 already cached, now input 30. should read directly from cache and no update on max_sn_cached
     * @throws Exception
     */
    @Test
    public void testGenerateAlreadyCached() throws Exception {
        int cachedSN = 40;
        int sn = 30;

        // Prepare
        writeFiboToCache(40);

        // Test start
        FiboSequenceGeneratorRedis fiboGen = new FiboSequenceGeneratorRedis();
        fiboGen.generateAndCache(sn);

        // Verify
        verifyResult(sn);
        verifyMaxFiboSNCached(cachedSN);
    }

    private void writeFiboToCache(int writtenSN) {
        for (int i = 3; i <= fiboSeq.size()-1; i++) {
            jedis.set(Integer.toString(i), fiboSeq.get(i).toString());
        }
        jedis.set(FiboSequenceGeneratorRedis.KEY_MAX_SN_CACHED, Integer.toString(writtenSN));
    }

    private void verifyMaxFiboSNCached(int expectedMaxCachedSN) {
        int max_fibo_cached = Integer.parseInt(jedis.get(FiboSequenceGeneratorRedis.KEY_MAX_SN_CACHED));
        Assert.assertEquals("Cached max_fibo_cached is not equal to expected one", expectedMaxCachedSN, max_fibo_cached);
    }

    public void verifyResult(int sn) {
        // Validation starts from 3 as the first 2 are not in cache.
        for (int i = 3; i <= sn; i++) {
            String strVal = jedis.get(Integer.toString(i));
            Assert.assertNotNull("The value of sn " + i + "should NOT be null.", strVal);
            BigInteger value = new BigInteger(strVal);
            Assert.assertEquals("Fibo value is not expected. Current sn = " + i, fiboSeq.get(i), value);
        }
    }
}

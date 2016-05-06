package myapp.fibo.executor;

import myapp.fibo.testutil.Timer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;

public class FiboSequenceGeneratorRedisTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testComputeFibo() {
        int sn = 100000;

        Jedis jedis = new Jedis("10.247.99.40");

        BigInteger fn_2 = BigInteger.ZERO;
        BigInteger fn_1 = BigInteger.ONE;
        BigInteger fn = BigInteger.ZERO;

        for (int i = 2; i < sn; i++) {
            fn = fn_2.add(fn_1);

            jedis.set(new Integer(i).toString(), fn.toString());

            // shift forward
            fn_2 = fn_1;
            fn_1 = fn;
        }

        jedis.close();
    }

    @Test
    public void testLoadFiboFromCache() throws Exception {
        int sn = 1000;

        Jedis jedis = new Jedis("10.247.99.40");

        Timer timer = new Timer();
        BufferedWriter writer = new BufferedWriter(new FileWriter("./test.result"));
        try {
            for (int i = 2; i < sn; i++) {
                writer.write(jedis.get(new Integer(i).toString()));
            }
        } finally {
            writer.close();
            jedis.close();
        }

        System.out.println("Spent " + timer.end());

    }
}

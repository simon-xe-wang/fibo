package myapp.fibo.service.resource;

import junit.framework.TestCase;
import myapp.fibo.seqstore.FiboSequenceStoreRedis;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.math.BigInteger;

public class FiboSequenceReaderRedisTest {

    /**
     * Input a sn, supposing already cached in store. Expect return a correct fibo sequence.
     * @throws Exception
     */
    @Test
    public void testRead() throws Exception {
        int sn = 10;

        writeFiboToStore(sn);


        FiboSequenceReaderRedis seqReader = new FiboSequenceReaderRedis(sn);
        try {
            String fiboVal;
            while ((fiboVal = seqReader.read()) != null) {
                System.out.println("Read a val " + fiboVal);

            }
        } finally {
            seqReader.close();
        }
    }

    private void writeFiboToStore(int sn) {

        FiboSequenceStoreRedis sequenceStore = new FiboSequenceStoreRedis();

        BigInteger fn_2 = BigInteger.ZERO;
        BigInteger fn_1 = BigInteger.ONE;
        BigInteger fn;

        try {
            int cachedSn = sequenceStore.getMaxCachedSn();
            if (sn - cachedSn <= 0) {
                return;
            }

            // Get the values of last 2 numbers cached
            if (cachedSn > 2) {
                fn_2 = sequenceStore.getFiboVal(cachedSn - 1);
                fn_1 = sequenceStore.getFiboVal(cachedSn);
            }

            // Append all non-cached sn
            int startPos = (cachedSn == 0) ? 1 : (cachedSn + 1);
            for (int i = startPos; i <= sn; i++) {
                fn = fn_2.add(fn_1);
                sequenceStore.writeFiboValue(i, fn);
                // shift forward
                fn_2 = fn_1;
                fn_1 = fn;
            }

            // Finally update max sn in cache
            sequenceStore.tryUpdateMaxCachedSn(sn);
        } finally {
            sequenceStore.close();
        }
   }
}
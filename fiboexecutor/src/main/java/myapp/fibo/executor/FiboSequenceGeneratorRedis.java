package myapp.fibo.executor;

import myapp.fibo.seqstore.FiboSequenceStore;
import myapp.fibo.seqstore.FiboSequenceStoreRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * Generate Fibonacci values to the cache. The computation against input number is based on the values cached in Redis.
 * For example, The fibonacci values of 0 - 1000 are already in cache, now input 1010, then only need to
 * compute 1001 - 1010 and then store corresponding values to cache for future use.
 */
public class FiboSequenceGeneratorRedis {
    private static Logger log = LoggerFactory.getLogger(FiboSequenceGeneratorRedis.class);

    FiboSequenceStore sequenceStore;

    public FiboSequenceGeneratorRedis() {
        sequenceStore = new FiboSequenceStoreRedis();
    }

    /**
     * Write Fibo values to cache
     * @param sn
     */
    public void generateAndCache(int sn) {
        BigInteger fn_2 = BigInteger.ZERO;
        BigInteger fn_1 = BigInteger.ONE;
        BigInteger fn;

        if (sn < 0) {
            throw new IllegalArgumentException("Invalid negative number");
        }

        try {
            int cachedSn = sequenceStore.getMaxCachedSn();
            if (sn - cachedSn <= 0) {
                log.info("Requested sn is already in cache. Skipping computing.");
                return;
            }

            // Get the values of last 2 numbers cached
            if (cachedSn > 2) { // the value of 3 in cache
                fn_2 = sequenceStore.getFiboVal(cachedSn - 1);
                fn_1 = sequenceStore.getFiboVal(cachedSn);
            }

            // Append all non-cached sn
            if (cachedSn <= 1) {
                sequenceStore.writeFiboValue(1, BigInteger.ZERO);
                sequenceStore.writeFiboValue(2, BigInteger.ONE);
            }

            int startPos = (cachedSn == 0) ? 3 : (cachedSn + 1);
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

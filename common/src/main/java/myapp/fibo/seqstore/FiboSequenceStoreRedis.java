package myapp.fibo.seqstore;

import myapp.fibo.FiboConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.math.BigInteger;

public class FiboSequenceStoreRedis implements FiboSequenceStore {
    private static Logger log = LoggerFactory.getLogger(FiboSequenceStoreRedis.class);

    public static final String KEY_MAX_SN_CACHED = "max_sn_cached";

    private Jedis jedis;

    public FiboSequenceStoreRedis() {
        jedis = new Jedis(FiboConfig.getInstance().getRedisHost());
    }

    @Override
    public int getMaxCachedSn() {
        String strMaxFiboCached = jedis.get(KEY_MAX_SN_CACHED);
        if (strMaxFiboCached == null) {
            return 0;
        }

        try {
            return Integer.parseInt(strMaxFiboCached);
        } catch (NumberFormatException e) {
            log.warn("Invalid format of max fibo cached. Deleting that ...");
            jedis.del(strMaxFiboCached);
            return 0;
        }
    }

    @Override
    public void writeFiboValue(int sn, BigInteger val) {
        jedis.set(Integer.toString(sn), val.toString());
    }

    @Override
    public void tryUpdateMaxCachedSn(int sn) {
        jedis.set(KEY_MAX_SN_CACHED, Integer.toString(sn));
    }

    @Override
    public BigInteger getFiboVal(int sn) {
        return new BigInteger( jedis.get(Integer.toString(sn) ) );
    }

    @Override
    public void close() {
        jedis.close();
    }
}

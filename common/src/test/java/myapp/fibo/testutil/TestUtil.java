package myapp.fibo.testutil;

import junit.framework.Assert;
import myapp.fibo.FiboTask;
import myapp.fibo.IdGeneratorTimeImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    /**
     * The general util method to verify result from a file. Do the following checks:
     * 1. Each number must be >=0 (not starting with '-')
     * 2. F(N) = F(N-2) + F(N-1)
     * @param count
     * @throws Exception
     */
    public static void verifyResultFromFile(int count, String resultFileName) throws Exception {
        ResultFileReader in = null;
        try {
            in = new ResultFileReader(resultFileName);

            BigInteger fn_2 = in.readNum();
            BigInteger fn_1 = in.readNum();

            for (int i = 2; i < count; i++) {
                BigInteger fn = in.readNum();
                Assert.assertEquals("Fibo value is incorrect", fn, fn_1.add(fn_2));
                // each one should be >0
                Assert.assertFalse("Negative value in stream", fn.toString().startsWith("-"));

                fn_2 = fn_1;
                fn_1 = fn;
            }

            // Should have nothing left in file
            Assert.assertNull("More numbers than expected in stream", in.readNum());
        } finally {
            in.close();
        }
    }

    public static List<BigInteger> getFiboSeq(int sn) {
        List<BigInteger> fibos = new ArrayList<>();
        BigInteger fn_2 = BigInteger.ZERO;
        BigInteger fn_1 = BigInteger.ONE;
        BigInteger fn = BigInteger.ZERO;

        // Dummy, just a state holder
        fibos.add(BigInteger.ZERO);

        // 1 and 2
        fibos.add(BigInteger.ZERO);
        fibos.add(BigInteger.ONE);

        for (int i = 3; i <= sn; i++) {
            fn = fn_2.add(fn_1);
            fibos.add(fn);
            // shift forward
            fn_2 = fn_1;
            fn_1 = fn;
        }

        return fibos;
    }

    public static FiboTask createTask(int stateReady, int sn) {
        FiboTask task = new FiboTask();
        task.setId(new IdGeneratorTimeImpl().gen());
        task.setSn(sn);
        task.setState(FiboTask.STATE_INPROGRESS);
        return task;
    }

    /**
     * Util class to read a sequence of numbers from a file.
     * Get null when reaching EOF. Throw exception if meeting any non-number.
     */
    private static class ResultFileReader {
        private char[] buf = new char[1024*1024]; // the size of the big number, suppose its big enough for testing purpose.
        private FileReader in;

        public ResultFileReader(String fileName) throws Exception {
            this.in = new FileReader(fileName);
        }

        public BigInteger readNum() throws Exception {
            int c;
            int idx = 0; // the pointer to each digit of a number
            while(true) {
                c = in.read();

                if (c == -1) { // file end
                    break;
                }

                if (c == ' ') { // space
                    if (idx > 0) { // already got a number so return it
                        break;
                    } else { // nothing got so continue reading
                        continue;
                    }
                }

                // Suppose if not space then must be a digit. will throw exception when building BigInteger later if not a digit
                buf[idx] = (char) c;
                idx ++;
            }

            // Nothing left.
            if (idx == 0) return null;

            return new BigInteger(new String(buf, 0, idx));
        }

        public void close() throws Exception {
            if (in != null) {
                in.close();
            }
        }
    }

    public static FileOutputStream createResultFileOutputStream(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();
        return new FileOutputStream(file);
    }
}

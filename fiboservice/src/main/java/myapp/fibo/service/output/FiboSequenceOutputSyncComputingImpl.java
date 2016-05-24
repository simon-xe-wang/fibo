package myapp.fibo.service.output;

import myapp.fibo.FiboSequenceIterator;
import myapp.fibo.FiboValueHandler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;

public class FiboSequenceOutputSyncComputingImpl implements StreamingOutput {
    private int sn;

    public FiboSequenceOutputSyncComputingImpl(int sn) {
        this.sn = sn;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        BigInteger fn_2 = BigInteger.ZERO;
        BigInteger fn_1 = BigInteger.ONE;
        BigInteger fn;

        Writer writer = new OutputStreamWriter(outputStream);

        if (sn < 0) {
            throw new IllegalArgumentException("Invalid negative number");
        }

        if (sn >= 1) {
            writer.write(BigInteger.ZERO.toString());
            writer.write(" ");
        }
        if (sn >= 2) {
            writer.write(BigInteger.ONE.toString());
            writer.write(" ");
        }
        for (int i = 3; i <= sn; i++) {
            fn = fn_2.add(fn_1);
            writer.write(fn.toString());
            writer.write(" ");

            fn_2 = fn_1;
            fn_1 = fn;
        }
        writer.flush();
    }
}

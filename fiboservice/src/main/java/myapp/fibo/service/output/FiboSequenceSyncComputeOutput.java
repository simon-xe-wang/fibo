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

public class FiboSequenceSyncComputeOutput implements StreamingOutput {
    private int sn;

    public FiboSequenceSyncComputeOutput(int sn) {
        this.sn = sn;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        FiboSequenceIterator fiboItr = new FiboSequenceIterator(sn);
        fiboItr.setHandler(new FiboValueSyncHandler(outputStream));
        fiboItr.run();
    }

    private static class FiboValueSyncHandler implements FiboValueHandler {
        private Writer writer;

        public FiboValueSyncHandler(OutputStream outputStream) {
            this.writer = new OutputStreamWriter(outputStream);
        }

        @Override
        public void handle(int sn, BigInteger val) {
            try {
                writer.write(val.toString());
                writer.write(" ");
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException("Error to write fibo value " + val, e);
            }
        }

        @Override
        public void handle(int sn, String val) {
            try {
                writer.write(val);
                writer.write(" ");
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException("Error to write fibo value " + val, e);
            }
        }
    }
}

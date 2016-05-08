/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package myapp.fibo.service.output;

import myapp.fibo.FiboTask;
import myapp.fibo.service.resource.FiboSequenceReaderRedis;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

/**
 * Streaming read Fibonacci sequence from FiboSequenceReaderRedis and write to output stream.
 */
public class FiboSequenceOutput implements StreamingOutput {

    private FiboSequenceReaderRedis fiboReader;

    public FiboSequenceOutput(FiboSequenceReaderRedis fiboReader) {
        this.fiboReader = fiboReader;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        try {
            // write status first
            writer.write(FiboTask.STATE_READY);
            // write data
            String fiboVal = null;
            while ((fiboVal = fiboReader.read()) != null) {
                writer.write(fiboVal);
                writer.write(" ");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fiboReader.close();
            writer.close();
        }
    }
}

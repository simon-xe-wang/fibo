package myapp.util;

import myapp.fibo.FiboConfig;
import myapp.fibo.FiboTask;
import myapp.fibo.testutil.TestUtil;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * The client wrapper for testing convenience.
 */
public class FiboClient {

    private static final int FILE_READ_BUF_SIZE = 1024 * 8;
    private WebTarget rootTarget;

    public FiboClient() {
        Client c = ClientBuilder.newClient();
        c.register(new LoggingFilter());
        rootTarget = c.target(FiboConfig.getInstance().getBaseURI());
    }

    /**
     * Get fibonacci sequence and store it into the specified file
     * @param sn
     * @param resultFilePath
     * @throws Exception
     */
    public void getFiboAsyncAsFile(int sn, String resultFilePath) throws Exception {

        // Get a task id
        WebTarget fibTarget = rootTarget.path("fibo").queryParam("sn", sn);
        Invocation.Builder invocationBuilder = fibTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        int httpStatus = response.getStatus();
        if (httpStatus < 200 || httpStatus >= 300) {
            throw new Exception("Request failed. Status code is " + httpStatus);
        }

        FiboTask task = response.readEntity(FiboTask.class);
        System.out.println("Get a task. Task id is " + task.getId());

        // Get Fibo Sequence by task id
        WebTarget fiboTaskTarget = rootTarget.path("fibo/task").queryParam("id", task.getId());
        invocationBuilder = fiboTaskTarget.request(MediaType.APPLICATION_OCTET_STREAM);

        while (true) {
            response = invocationBuilder.get();

            httpStatus = response.getStatus();
            if (httpStatus < 200 || httpStatus >= 300) {
                throw new Exception("Request failed. Status code is " + httpStatus);
            }

            InputStream rspStream = response.readEntity(InputStream.class);
            try {
                // read the first byte which is task status
                int taskStatus = rspStream.read();
                if (taskStatus != FiboTask.STATE_READY) { // Not ready
                    System.out.println("Task not ready, waiting 10 seconds and retry...");
                    Thread.sleep(10*1000);
                    continue;
                } else { // Ready. Write to file
                    FileOutputStream rtResultFile = null;
                    try {
                        rtResultFile = TestUtil.createResultFileOutputStream(resultFilePath);
                        byte[] buf = new byte[FILE_READ_BUF_SIZE];
                        int nRead = 0;
                        while ((nRead = rspStream.read(buf)) > 0) {
                            rtResultFile.write(buf, 0, nRead);
                        }
                    } finally {
                        rtResultFile.close();
                    }
                    break;
                }
            } finally {
                rspStream.close();
            }
        }
    }

    public static void main(String[] args) {
        FiboClient client = new FiboClient();
        String resultFile = "./results/100_result";
        int sn = 100;
        try {
            client.getFiboAsyncAsFile(sn, resultFile);
            System.out.printf("Done, find the result at " + resultFile);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}

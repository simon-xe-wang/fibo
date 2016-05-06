package myapp.fibo;

import myapp.fibo.service.Main;
import myapp.util.FiboClient;
import myapp.util.TestUtil;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FiboResourceTest {

    private HttpServer server;
    private String resTestResultFile = "./results/rtResult";
    FiboClient fiboClient;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();

        fiboClient = new FiboClient();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testBigSN() throws Exception {
        int sn = 10000;

        fiboClient.getFiboAsyncAsFile(sn, resTestResultFile);
        TestUtil.verifyResultFromFile(sn, resTestResultFile);
    }
}

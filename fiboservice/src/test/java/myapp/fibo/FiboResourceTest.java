package myapp.fibo;

import myapp.fibo.service.Main;
import myapp.util.TestUtil;
import myapp.fibo.client.FiboClient;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.*;

import static org.junit.Assert.assertEquals;

public class FiboResourceTest {

    private static HttpServer server;
    private static String resTestResultFile = "./results/rtResult";
    private static FiboClient fiboClient;

    @BeforeClass
    public static void setUp() throws Exception {
        // start the server
     //   server = Main.startServer();

        fiboClient = new FiboClient();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    //    server.stop();
    }

    /**
     * Basic function test. Input sn 100, expect get a task and poll its status and once ready get a stream, store to local file and verify value
     */
    @Test
    public void testGetFibo() throws Exception {
        int sn = 100;

        fiboClient.getFiboAsyncAsFile(sn, resTestResultFile);
        TestUtil.verifyResultFromFile(sn, resTestResultFile);
    }

    /**
     * Basic function test. Sending 10 requests and expect get results
     */
    @Test
    public void testGetBatchFibo() throws Exception {
        int sn = 100;
        int count = 10;

        for (int i = 0; i < count; i++) {
            fiboClient.getFiboAsyncAsFile(sn+i, resTestResultFile);
            TestUtil.verifyResultFromFile(sn + i, resTestResultFile);
            System.out.println("Verified " + i);
        }
    }

    /**
     * Invalid argument. Input a negative value. Expect get a clear error message.
     */
    @Test(expected = Exception.class)
    public void testNegativeSn() throws Exception {
        int sn = -100;
        fiboClient.getFiboAsyncAsFile(sn, resTestResultFile);
    }
}

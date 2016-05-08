package myapp.fibo.service.resource;

import myapp.fibo.service.BeanFactory;
import myapp.fibo.service.executor.FiboExecutorClient;
import myapp.fibo.FiboTask;
import myapp.fibo.IdGenerator;
import myapp.fibo.taskstore.FiboTaskStore;
import myapp.fibo.taskstore.FiboTaskStoreRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

/**
 * Fibonacci wrapper resource (exposed at "fibo" path)
 */
@Path("/fibo")
public class FiboAsyncResource {

    private static Logger log = LoggerFactory.getLogger(FiboAsyncResource.class);

    IdGenerator idGenerator = BeanFactory.getInstance().getIdGenerator();
    private FiboExecutorClient executorClient = BeanFactory.getInstance().getExecutorClient();
    private FiboTaskStore taskStore = new FiboTaskStoreRedis();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM})
    public FiboTask getFiboAsync(@QueryParam("sn") int sn) throws Exception {
        log.info("Received a request to get Sequence, sn = {}", sn);

        checkParam(sn);

        FiboTask task = createTask(sn);
        taskStore.save(task);
        executorClient.submit(task);

        log.info("Submitted a task to executor id = {}, sn = {}", task.getId(), sn);
        return task;
    }

    private void checkParam(int sn) {
        if (sn < 0) {
            throw new InvalidRequestException("Negative value is not accepted.");
        }
    }

    private FiboTask createTask(int sn) {
        FiboTask task = new FiboTask();
        task.setId(idGenerator.gen());
        task.setSn(sn);
        task.setState(FiboTask.STATE_INPROGRESS);
        return task;
    }
}

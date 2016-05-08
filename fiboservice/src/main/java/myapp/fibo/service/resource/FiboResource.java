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
public class FiboResource {

    private static Logger log = LoggerFactory.getLogger(FiboResource.class);

    IdGenerator idGenerator = BeanFactory.getInstance().getIdGenerator();
    private FiboExecutorClient executorClient = BeanFactory.getInstance().getExecutorClient();
    private FiboTaskStore taskStore = new FiboTaskStoreRedis();

    @GET
    // @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM})
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getFibo(
            @QueryParam("sn") int sn,
            @DefaultValue("false") @QueryParam("sync") boolean sync) throws Exception {
        log.info("Received a request to get Sequence, sn = {}, sync = {}", sn, sync);

        checkParam(sn);

        if (sync) {
            return getFiboSync(sn);
        } else {
            return getFiboAsync(sn);
        }
    }

    public Response getFiboAsync(int sn) throws Exception {
        FiboTask task = createTask(sn);
        taskStore.save(task);
        executorClient.submit(task);

        log.info("Submitted a task to executor id = {}, sn = {}", task.getId(), sn);
        return Response.ok(task).build();
    }

    public Response getFiboSync(int sn) {
        FiboSequenceOutput2 output = new FiboSequenceOutput2(sn);
        return Response.ok(output).build();
    }

    /**
     *
     * @param id
     * @return
     */
    @GET     @Path("/task")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.TEXT_PLAIN})
    public Response fetchFiboSeq(@QueryParam("id") String id) {

        FiboTask task = taskStore.query(id);
        if (taskDone(task)) {
            FiboSequenceReaderRedis fiboReader = new FiboSequenceReaderRedis(task.getSn());
            StreamingOutput seqStream = new FiboSequenceOutput(fiboReader);
            return Response.ok(seqStream).build();
        }

        return Response.ok(new TaskStreamOutput(task)).build();
    }

    private void checkParam(int sn) {
        if (sn < 0) {
            throw new InvalidRequestException("Negative value is not accepted.");
        }
    }

    private boolean taskDone(FiboTask task) {
        return task.getState() == FiboTask.STATE_READY;
    }

    private FiboTask createTask(int sn) {
        FiboTask task = new FiboTask();
        task.setId(idGenerator.gen());
        task.setSn(sn);
        task.setState(FiboTask.STATE_INPROGRESS);
        return task;
    }

    private class TaskStreamOutput implements StreamingOutput {
        private FiboTask task;

        public TaskStreamOutput(FiboTask task) {
            this.task = task;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException, WebApplicationException {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            try {
                writer.write(FiboTask.STATE_INPROGRESS);
                writer.write(task.getId());
            } finally {
                writer.flush();
                writer.close();
            }

        }
    }
}

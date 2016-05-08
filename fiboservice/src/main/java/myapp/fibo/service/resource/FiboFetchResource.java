package myapp.fibo.service.resource;

import myapp.fibo.FiboTask;
import myapp.fibo.service.output.FiboSequenceOutput;
import myapp.fibo.taskstore.FiboTaskStore;
import myapp.fibo.taskstore.FiboTaskStoreRedis;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

@Path("/fibotask")
public class FiboFetchResource {

    private FiboTaskStore taskStore = new FiboTaskStoreRedis();

    /**
     * Fetch Fibo Sequence by task id
     * @param id
     * @return
     */
    @GET
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

    private boolean taskDone(FiboTask task) {
        return task.getState() == FiboTask.STATE_READY;
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

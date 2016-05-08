package myapp.fibo.service.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fibosync")
public class FiboSyncResource {
    private static Logger log = LoggerFactory.getLogger(FiboSyncResource.class);

    @GET
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getFiboSync(@QueryParam("sn") int sn) {
        log.info("Received a request to get Sequence, sn = {}", sn);

        checkParam(sn);

        FiboSequenceOutput2 output = new FiboSequenceOutput2(sn);
        return Response.ok(output).build();
    }

    @GET @Path("/cache")
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getFiboSyncFromCache(@QueryParam("sn") int sn) {
        log.info("Received a request to get Sequence, sn = {}", sn);

        checkParam(sn);

        FiboSequenceOutput3 output = new FiboSequenceOutput3(sn);
        return Response.ok(output).build();
    }

    private void checkParam(int sn) {
        if (sn < 0) {
            throw new InvalidRequestException("Negative value is not accepted.");
        }
    }
}

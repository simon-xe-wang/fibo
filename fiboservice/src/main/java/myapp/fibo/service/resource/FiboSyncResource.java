package myapp.fibo.service.resource;

import myapp.fibo.seqstore.FiboSequenceStore;
import myapp.fibo.seqstore.FiboSequenceStoreRedis;
import myapp.fibo.service.output.FiboSequenceSyncComputeOutput;
import myapp.fibo.service.output.FiboSequenceSyncOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fibo2")
public class FiboSyncResource {
    private static Logger log = LoggerFactory.getLogger(FiboSyncResource.class);

    /**
     * This api always try to get fibo from store. If not found then return no content. Not ready for production use.
     * @param sn
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getFiboSyncFromCache(@QueryParam("sn") int sn) {
        log.info("Received a request to get Sequence, sn = {}", sn);

        checkParam(sn);

        FiboSequenceStore sequenceStore = new FiboSequenceStoreRedis();
        if (!alreadyCached(sequenceStore, sn)) {
            return Response.noContent().build();
        }
        sequenceStore.close();

        FiboSequenceSyncOutput output = new FiboSequenceSyncOutput(sn);
        return Response.ok(output).build();
    }

    private boolean alreadyCached(FiboSequenceStore sequenceStore, int sn) {
        return sequenceStore.getMaxCachedSn() >= sn;
    }

    /**
     * This one computes Fibo and return directly. Just for testing and not used by production.
     * @param sn
     * @return
     */
    @GET @Path("/compute")
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getFiboSync(@QueryParam("sn") int sn) {
        log.info("Received a request to get Sequence, sn = {}", sn);

        checkParam(sn);

        FiboSequenceSyncComputeOutput output = new FiboSequenceSyncComputeOutput(sn);
        return Response.ok(output).build();
    }

    private void checkParam(int sn) {
        if (sn < 0) {
            throw new InvalidRequestException("Negative value is not accepted.");
        }
    }
}

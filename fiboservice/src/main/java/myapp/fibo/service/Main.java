package myapp.fibo.service;

import myapp.fibo.FiboConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {

    // Add any packages contaning web resources here
    public static String[] servicePackages = new String[] {
            "myapp.fibo.service.resource"
    };

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in test package
        final ResourceConfig rc = new ResourceConfig().packages(servicePackages);

        URI baseUri = FiboConfig.getInstance().getBaseURI();

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        startServer();
        String msg = String.format("FiboService started at %s.", FiboConfig.getInstance().getBaseURI());
        System.out.println(msg);
    }
}


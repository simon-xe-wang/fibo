package myapp.fibo;


import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * The config class containing all configuration information
 */
public class FiboConfig {

    private static final String CONFIG_FILE_PATH = "fibo.yaml";
    private String host;
    private int port;
    private String redisHost;
    private String topic;
    private String kafkaBootupServer;
    private String kafkaConsumerGroup;

    public static FiboConfig getInstance() {
        return loadConfigFromFile();
    }

    private static FiboConfig loadConfigFromFile() {
        InputStream in = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_PATH);
        if (in == null) {
            throw new RuntimeException("Unable to locate the config file " + CONFIG_FILE_PATH + ". Make sure the conf directory is added into class path");
        }

        YamlReader configReader = null;
        try {
            configReader = new YamlReader(new InputStreamReader(in));
            return configReader.read(FiboConfig.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private FiboConfig() {}

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public URI getBaseURI() {
        String uri = String.format("http://%s:%d/myapp", host, port);
        return URI.create(uri);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRedisHost() {
        return this.redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getKafkaBootStrapServers() {
        return this.kafkaBootupServer;
    }

    public Object getKafkaConsumerGroup() {
        return this.kafkaConsumerGroup;
    }

    public void setKafkaBootupServer(String kafkaBootupServer) {
        this.kafkaBootupServer = kafkaBootupServer;
    }

    public void setKafkaConsumerGroup(String kafkaConsumerGroup) {
        this.kafkaConsumerGroup = kafkaConsumerGroup;
    }
}

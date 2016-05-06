package myapp.fibo.service.executor;

import myapp.fibo.FiboConfig;
import myapp.fibo.FiboTask;
import myapp.fibo.utils.JsonUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * When submitting a task, the client will generate a new task and store it to Redis and send it backend executor by Kafka.
 */
public class FiboExecutorClientKafka implements FiboExecutorClient {

    private static Logger log = LoggerFactory.getLogger(FiboExecutorClientKafka.class);

    private static Producer<String, String> producer = null;
    String taskTopic = FiboConfig.getInstance().getTopic();

    @Override
    public void submit(FiboTask task) throws Exception {
        getProducer().send(new ProducerRecord<>(taskTopic, task.getId(), JsonUtils.taskToJson(task))).get();
        log.info("Submitted a task", task);
    }

    private Producer<String, String> getProducer() {
        if (producer != null) {
            return producer;
        }

        Properties props = new Properties();
        props.put("bootstrap.servers", FiboConfig.getInstance().getKafkaBootStrapServers());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
        return producer;
    }
}

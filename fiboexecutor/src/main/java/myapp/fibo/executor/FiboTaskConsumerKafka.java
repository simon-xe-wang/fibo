package myapp.fibo.executor;

import myapp.fibo.FiboConfig;
import myapp.fibo.FiboTask;
import myapp.fibo.utils.JsonUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;


public class FiboTaskConsumerKafka implements Runnable {
    private static Logger log = LoggerFactory.getLogger(FiboTaskConsumerKafka.class);

    private static final long CONSUMERR_POLL_INTERVAL_MSEC = 2000;

    private Consumer<String, String> consumer;
    private FiboTaskHandler taskHandler = new FiboTaskHandlerRedis();

    public FiboTaskConsumerKafka() {
        buildConsumer(); // Every time build a new consumer as Kafka consumer is not thread safe
    }

    /**
     * Build a consumer with manual commit
     */
    private void buildConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", FiboConfig.getInstance().getKafkaBootStrapServers());
        props.put("group.id", FiboConfig.getInstance().getKafkaConsumerGroup());
        props.put("enable.auto.commit", "false");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(FiboConfig.getInstance().getTopic()));
    }

    @Override
    public void run() {
        String topic = FiboConfig.getInstance().getTopic();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(CONSUMERR_POLL_INTERVAL_MSEC);
            log.info("Received {} records", records.count());
            for (ConsumerRecord<String, String> record : records) {

                FiboTask task = null;
                try {
                    task = JsonUtils.jsonToTask(record.value());
                } catch (Exception e) { // Invalid task. Log and ignore
                    log.warn("Got a invalid task representation: {}. Skipping ...", record.value());
                    continue;
                }

                try {
                    taskHandler.handle(task);
                    // Commit manually. This way to make sure the consumer node is down other consumers can consume this record.
                    // Using this commitSync() without parameter might cause tasks resend when rebalancing happens. Application
                    // Will handle this based on task status in db.
                    consumer.commitSync();
                } catch (Exception e) { // Issue when process the task
                    // Seek to the offset of current record. So next poll can get the record again.
                    // Also skip remaining tasks as they will come again
                    consumer.seek(new TopicPartition(topic, 0), record.offset());
                    log.info("Error to handle task {} whose offset is {}. Seek and ignore remaining tasks.",
                            task.getId(), record.offset());
                    break;
                }
            }
        }
    }
}

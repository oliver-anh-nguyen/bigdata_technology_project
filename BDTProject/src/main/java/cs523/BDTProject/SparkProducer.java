package cs523.BDTProject;

import org.apache.kafka.clients.producer.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SparkProducer {

    public static void main(String[] args) throws Exception {
        Logger.getLogger("kafka").setLevel(Level.ALL);

        Producer<String, String> producer = new KafkaProducer<>(Constants.KAFKA_PRODUCER_PROPERTIES);

        Parser.readAndPublishToKafka(producer);

        producer.close();
    }

    public static void publishToKafka(Producer<String, String> producer, String message) {
        producer.send(new ProducerRecord<>(Constants.TOPIC_NAME, message), (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            }
        });
        producer.flush();
    }
}

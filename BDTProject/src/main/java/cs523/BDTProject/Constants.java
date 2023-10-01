package cs523.BDTProject;

import java.util.Properties;

import org.apache.hadoop.hbase.util.Bytes;

public class Constants {
    public static final String KAFKA_SERVER = "localhost:9092";
    public static final String CSV_FILE_PATH = "src/main/resources/adult.csv";
    public static final String TOPIC_NAME = "group20";
    public static final String TABLE_NAME = "adult";
    public static final String CF_DATA = "data";
    
    public static final byte[] COL_AGE = Bytes.toBytes("Age");
    public static final byte[] COL_OCCUPATION = Bytes.toBytes("Occupation");
    public static final byte[] COL_GENDER = Bytes.toBytes("Gender");
    public static final byte[] COL_COUNTRY = Bytes.toBytes("Country");
    
    // SparkSession Configuration
    public static final String APP_NAME = "SparkHBase";
    public static final String MASTER = "local[*]";
    
    public static final Properties KAFKA_PRODUCER_PROPERTIES = new Properties();
    static {
        KAFKA_PRODUCER_PROPERTIES.put("metadata.broker.list", KAFKA_SERVER);
        KAFKA_PRODUCER_PROPERTIES.put("bootstrap.servers", KAFKA_SERVER);
        KAFKA_PRODUCER_PROPERTIES.put("acks", "all");
        KAFKA_PRODUCER_PROPERTIES.put("retries", 0);
        KAFKA_PRODUCER_PROPERTIES.put("linger.ms", 1);
        KAFKA_PRODUCER_PROPERTIES.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PRODUCER_PROPERTIES.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }
}
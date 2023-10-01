package cs523.BDTProject;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;

public class SparkHBase {

    public static void main(String... args) {
        Logger.getLogger("org").setLevel(Level.ERROR);

        try {
        	System.out.println("Create table!");
            HBaseOperations.createTable(Constants.TABLE_NAME, Constants.CF_DATA);
            System.out.println("Done table!");
            SparkSession spark = SparkSession.builder()
                    .appName(Constants.APP_NAME)
                    .master(Constants.MASTER).getOrCreate();

            String kafkaServer = Constants.KAFKA_SERVER;
            Dataset<Row> ds = spark.readStream().format("kafka")
                    .option("kafka.bootstrap.servers", kafkaServer)
                    .option("subscribe", Constants.TOPIC_NAME)
                    .load();

            ds.printSchema();            
            Dataset<Row> lines = ds.selectExpr("CAST(value AS STRING)");

            Dataset<Row> dataAsSchema = lines
            	    .selectExpr(
            	        "CAST(value AS STRING)",
            	        "split(value, ',')[0] AS Age",
            	        "split(value, ',')[1] AS Occupation",
            	        "split(value, ',')[2] AS Gender",
            	        "split(value, ',')[3] AS Country"
            	    )
            	    .drop("value");


            ForeachWriter<Row> customForeachWriter = new ImportForeachWriter();
            dataAsSchema.coalesce(1).writeStream()
                    .queryName("Write data")
                    .outputMode(OutputMode.Update())
                    .trigger(Trigger.ProcessingTime(30))
                    .foreach(customForeachWriter)
                    .start()
                    .awaitTermination();

            System.out.println("Done!");

        } catch (IOException | StreamingQueryException e) {
            e.printStackTrace();
        }
    }
}

@SuppressWarnings("serial")
class ImportForeachWriter extends ForeachWriter<Row> {

    @Override
    public boolean open(long partitionId, long version) {
        // Initialize any resources needed for writing (e.g., database connections)
        return true;
    }

    @Override
    public void process(Row row) {
        Configuration config = HBaseConfiguration.create();

        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {

            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(Constants.TABLE_NAME));
            table.addFamily(new HColumnDescriptor(Constants.CF_DATA).setCompressionType(Algorithm.NONE));

            Table hTable = connection.getTable(table.getTableName());
            byte[] dataCF = Bytes.toBytes(Constants.CF_DATA);

            System.out.println("Data adding: " + row.toString());

            Put put = new Put(Bytes.toBytes(row.getString(0)));
            put.addColumn(dataCF, Constants.COL_AGE, Bytes.toBytes(row.getString(0)));
            put.addColumn(dataCF, Constants.COL_OCCUPATION, Bytes.toBytes(row.getString(1)));
            put.addColumn(dataCF, Constants.COL_GENDER, Bytes.toBytes(row.getString(2)));
            put.addColumn(dataCF, Constants.COL_COUNTRY, Bytes.toBytes(row.getString(3)));

            hTable.put(put);
            hTable.close();

            System.out.println("Data added");

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void close(Throwable errorOrNull) {
        // Clean up resources if needed
    }
}

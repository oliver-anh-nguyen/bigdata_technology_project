package cs523.BDTProject;

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

import java.io.IOException;

public class HBaseOperations {

    public static void createTable(String tableName, String columnFamily) throws IOException {
        Configuration config = HBaseConfiguration.create();

        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {

            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));
            table.addFamily(new HColumnDescriptor(columnFamily).setCompressionType(Algorithm.NONE));

            if (admin.tableExists(table.getTableName())) {
                admin.disableTable(table.getTableName());
                admin.deleteTable(table.getTableName());
            }
            admin.createTable(table);
        }
    }

    public static void putRow(String tableName, String columnFamily, String rowKey, String[] columnNames, String[] values) throws IOException {
        Configuration config = HBaseConfiguration.create();

        try (Connection connection = ConnectionFactory.createConnection(config)) {

            Table hTable = connection.getTable(TableName.valueOf(tableName));
            byte[] dataCF = Bytes.toBytes(columnFamily);

            for (int i = 0; i < columnNames.length; i++) {
                byte[] colName = Bytes.toBytes(columnNames[i]);
                Put put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(dataCF, colName, Bytes.toBytes(values[i]));
                hTable.put(put);
            }
        }
    }
}
package cs523.BDTProject;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

import org.apache.kafka.clients.producer.Producer;

public class Parser {

    public static AdultEntity parseCSVLine(String[] line) {
        String age = line[0];
        String occupation = line[6];
        String gender = line[9];
        String country = line[13];

        return new AdultEntity(age, occupation, gender, country);
    }

    public static void readAndPublishToKafka(Producer<String, String> producer) {
        try (CSVReader reader = new CSVReader(new FileReader(Constants.CSV_FILE_PATH))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    String age = nextLine[0];
                    String occupation = nextLine[6];
                    String gender = nextLine[9];
                    String country = nextLine[13];

                    AdultEntity entity = new AdultEntity(age, occupation, gender, country);

                    System.out.println("Row: " + entity);
                    SparkProducer.publishToKafka(producer, entity.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}

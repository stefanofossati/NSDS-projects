package NSDSprojects;

import NSDSprojects.preprocessors.AbstractPreprocessor;
import NSDSprojects.preprocessors.EcdcPreprocessor;
import org.apache.spark.sql.SparkSession;

public class Main {
    public static void main(String[] args) {
        final String master = args.length > 0 ? args[0] : "local[4]";
        final String inputDatasetPath = args.length > 1 ? args[1] : "./files/ecdc_data.csv";
        final boolean logOnConsole = args.length > 2 ? args[2].equals("true") : false;

        final SparkSession spark = SparkSession
                .builder()
                .master(master)
                .appName("Covid19DataAnalysis")
                .getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");

        AbstractPreprocessor preprocessor = new EcdcPreprocessor(spark, inputDatasetPath);
        preprocessor.loadAndPreprocess();


    }
}
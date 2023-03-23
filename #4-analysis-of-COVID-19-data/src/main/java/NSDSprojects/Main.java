package NSDSprojects;

import NSDSprojects.preprocessors.AbstractPreprocessor;
import NSDSprojects.preprocessors.EcdcPreprocessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Main {
    public static void main(String[] args) {
        final String master = args.length > 0 ? args[0] : "local[4]";
        final String inputDatasetPath = args.length > 1 ? args[1] : "./files/ecdc_data.csv";
        final boolean logOnConsole = args.length > 2 ? args[2].equals("true") : false;
        final boolean useCache = args.length > 3 ? Boolean.parseBoolean(args[3]) : true;

        final SparkSession spark = SparkSession
                .builder()
                .master(master)
                .appName("Covid19DataAnalysis")
                .getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");

        AbstractPreprocessor preprocessor = new EcdcPreprocessor(spark, inputDatasetPath);
        Dataset<Row> dataset = preprocessor.loadAndPreprocess();

        Dataset<Row> query1Results = QueryExecutor.sevenDaysMovingAverage(dataset);

        if(useCache)
            query1Results.cache();

        query1Results.show(20);

        Dataset<Row> query2Results = QueryExecutor.percentageIncreaseDayBeforeMovingAverage(query1Results);

        if(useCache)
            query2Results.cache();

        query2Results.show(20);

        query1Results.unpersist();

        Dataset<Row> query3Results = QueryExecutor.top10CountriesHighestPercIncrease(query2Results);

        if(useCache)
            query3Results.cache();

        query3Results.show(20);

        query2Results.unpersist();

        spark.close();
    }
}
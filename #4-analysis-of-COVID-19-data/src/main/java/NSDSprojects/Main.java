package NSDSprojects;

import NSDSprojects.preprocessors.AbstractPreprocessor;
import NSDSprojects.preprocessors.EcdcPreprocessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class Main {
    public static void main(String[] args) {
        final String master = args.length > 0 ? args[0] : "local[4]";
        final String inputDatasetPath = args.length > 1 ? args[1] : "./files/input/ecdc_data.csv";
        final boolean logOnConsole = args.length > 2 ? args[2].equals("true") : true;
        final boolean useCache = args.length > 3 ? Boolean.parseBoolean(args[3]) : true;
        final boolean writeToFile = args.length > 4 ? Boolean.parseBoolean(args[4]) : false;
        final String outputPath = "./files/output/";

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

        if(logOnConsole)
            query1Results.show(20);

        Dataset<Row> query2Results = QueryExecutor.percentageIncreaseDayBeforeMovingAverage(query1Results);

        if(useCache)
            query2Results.cache();

        if(logOnConsole)
            query2Results.show(20);

        query1Results.unpersist();

        Dataset<Row> query3Results = QueryExecutor.top10CountriesHighestPercIncrease(query2Results);

        if(useCache)
            query3Results.cache();

        if(logOnConsole)
            query3Results.show(20);

        query2Results.unpersist();

        if(writeToFile)
            query3Results.coalesce(1).write().mode(SaveMode.Overwrite).option("header", "true").csv(outputPath);

        spark.close();
    }
}
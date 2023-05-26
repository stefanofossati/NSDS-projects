package NSDSprojects;

import NSDSprojects.preprocessors.AbstractPreprocessor;
import NSDSprojects.preprocessors.EcdcPreprocessor;
import NSDSprojects.preprocessors.SyntheticDataPreprocessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class Main {
    public static void main(String[] args) {
        final String master = args.length > 0 ? args[0] : "local[4]";
        final String inputDatasetPath = args.length > 1 ? args[1] : "./files/input/ecdc_data.csv";
        final String datasetType = args.length > 2 ? args[2] : "ecdc";
        final boolean logOnConsole = args.length > 3 ? args[3].equals("true") : true;
        final boolean useCache = args.length > 4 ? Boolean.parseBoolean(args[4]) : true;
        final boolean writeToFile = args.length > 5 ? Boolean.parseBoolean(args[5]) : true;
        final String outputPath = args.length > 6 ? args[6] : "./files/output/";

        final SparkSession spark = SparkSession
                .builder()
                .master(master)
                .appName("Covid19DataAnalysis")
                .getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");

        AbstractPreprocessor preprocessor;

        if(datasetType.equals("ecdc")) {
            preprocessor = new EcdcPreprocessor(spark, inputDatasetPath);
        } else if(datasetType.equals("simul")) {
            preprocessor = new SyntheticDataPreprocessor(spark, inputDatasetPath);
        } else {
            System.out.println("Dataset type not supported");
            spark.close();
            return;
        }

        Dataset<Row> dataset = preprocessor.loadAndPreprocess();

        if(logOnConsole)
            dataset.show(50);

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
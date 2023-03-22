package NSDSprojects.preprocessors;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public abstract class AbstractPreprocessor {
    private final SparkSession sparkSession;
    private final String datasetPath;

    public AbstractPreprocessor(SparkSession sparkSession, String datasetPath) {
        this.sparkSession = sparkSession;
        this.datasetPath = datasetPath;
    }

    public abstract Dataset<Row> loadAndPreprocess();

    protected SparkSession getSparkSession() {
        return sparkSession;
    }

    protected String getDatasetPath() {
        return datasetPath;
    }
}

package NSDSprojects.preprocessors;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.functions.*;

public class SyntheticDataPreprocessor extends AbstractPreprocessor {

    public SyntheticDataPreprocessor(SparkSession sparkSession, String datasetPath) {
        super(sparkSession, datasetPath);
    }

    @Override
    public Dataset<Row> loadAndPreprocess() {
        final List<StructField> mySchemaFields = new ArrayList<>();
        mySchemaFields.add(DataTypes.createStructField("day", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("country_id", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("non_infected", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("infected", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("immune", DataTypes.IntegerType, false));

        final StructType schema = DataTypes.createStructType(mySchemaFields);

        Dataset<Row> dataset =  getSparkSession()
                .read()
                .option("header", "true")
                .option("delimiter", ",")
                .schema(schema)
                .csv(getDatasetPath());

        // convert number of total infected to differential daily new cases
        dataset = findNewDailyCases(dataset);

        // select only interesting columns and rename them
        dataset = dataset.select(
                col("day").as("date"),
                col("cases"),
                col("country_id").as("country")
        );

        return dataset;
    }

    private Dataset<Row> findNewDailyCases(Dataset<Row> dataset) {
        WindowSpec window = Window.partitionBy("country_id").orderBy("day");

        dataset = dataset
                .withColumn("diff_cases",
                        col("infected")
                                .minus(lag("infected", 1, 0).over(window)))
                .withColumn("cases",
                        when(col("diff_cases").$less(0), lit(0))
                                .otherwise(col("diff_cases")));

        return dataset;
    }
}

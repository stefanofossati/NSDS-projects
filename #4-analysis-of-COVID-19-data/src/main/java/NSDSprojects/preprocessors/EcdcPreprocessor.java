package NSDSprojects.preprocessors;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.to_date;

public class EcdcPreprocessor extends AbstractPreprocessor{

    public EcdcPreprocessor(SparkSession sparkSession, String datasetPath) {
        super(sparkSession, datasetPath);
    }

    @Override
    public Dataset<Row> loadAndPreprocess() {
        final List<StructField> mySchemaFields = new ArrayList<>();
        mySchemaFields.add(DataTypes.createStructField("date", DataTypes.StringType, false));
        mySchemaFields.add(DataTypes.createStructField("day", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("month", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("year", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("cases", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("deaths", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("country", DataTypes.StringType, false));
        mySchemaFields.add(DataTypes.createStructField("geoId", DataTypes.StringType, false));
        mySchemaFields.add(DataTypes.createStructField("countryCode", DataTypes.StringType, false));
        mySchemaFields.add(DataTypes.createStructField("population", DataTypes.IntegerType, false));
        mySchemaFields.add(DataTypes.createStructField("continent", DataTypes.StringType, false));
        mySchemaFields.add(DataTypes.createStructField("infectivityRate", DataTypes.FloatType, false));

        final StructType schema = DataTypes.createStructType(mySchemaFields);


        Dataset<Row> dataset =  getSparkSession()
                .read()
                .option("header", "true")
                .option("delimiter", ",")
                .schema(schema)
                .csv(getDatasetPath());

        // select only interesting columns
        dataset = dataset.select("date", "cases", "country", "continent");

        // convert string to date format for the first column
        dataset = dataset.withColumn("date", to_date(col("date"), "dd/MM/yyyy"));

        dataset.show();

        return dataset;
    }
}

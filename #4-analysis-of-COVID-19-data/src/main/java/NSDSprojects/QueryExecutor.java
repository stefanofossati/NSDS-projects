package NSDSprojects;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

import static org.apache.spark.sql.functions.avg;
import static org.apache.spark.sql.functions.col;

public class QueryExecutor {

    public static Dataset<Row> sevenDaysMovingAverage(Dataset<Row> dataset){

        WindowSpec window = Window
                .partitionBy("country")
                .orderBy("date")
                .rowsBetween(-6, Window.currentRow());

        return dataset.withColumn("movAvg", avg(col("cases")).over(window));
    }
}

package NSDSprojects;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

import static org.apache.spark.sql.functions.*;

public class QueryExecutor {

    public static Dataset<Row> sevenDaysMovingAverage(Dataset<Row> dataset){

        WindowSpec window = Window
                .partitionBy("country")
                .orderBy("date")
                .rowsBetween(-6, Window.currentRow());

        return dataset.withColumn("movAvg", avg(col("cases")).over(window));
    }

    public static Dataset<Row> percentageIncreaseDayBeforeMovingAverage(Dataset<Row> dataset){

        WindowSpec window = Window
                .partitionBy("country")
                .orderBy("date");

        return dataset
                .withColumn("percIncreaseMovAvg",
                        col("movAvg")
                                .minus(lag("movAvg", 1, 0).over(window))
                                .multiply(100.0)
                                .divide(col("movAvg"))
                );
    }

    public static Dataset<Row> top10CountriesHighestPercIncrease(Dataset<Row> dataset){

        WindowSpec window = Window
                .partitionBy("date")
                .orderBy(desc("percIncreaseMovAvg"));

        return dataset
                .withColumn("rank", row_number().over(window))
                .where(col("rank").$less$eq(10))
                .orderBy("date", "rank");
    }
}

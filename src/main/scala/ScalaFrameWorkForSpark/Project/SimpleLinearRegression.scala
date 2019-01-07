package ScalaFrameWorkForSpark.Project

import ScalaFrameWorkForSpark.SparkUtils.SparkConfig
import org.apache.spark.ml.feature.{Normalizer, StringIndexer, VectorAssembler}
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.evaluation.RegressionEvaluator

object SimpleLinearRegression {

  def main(args: Array[String]): Unit = {

    val spark=SparkConfig.SparkSn
    spark.sqlContext.implicits
    val training = spark.read.format("csv").option("header", "true").load("/Users/shuvamoymondal/Desktop/Salary_Data.csv")
    var df1 = training.select(training("Salary").cast("Double").as("lebel"),training("YearsExperience").cast("Double"))

    var YearsExperienceIndex= new StringIndexer().setInputCol("YearsExperience").setOutputCol("YearsExperienceIndexed")

    df1=YearsExperienceIndex.fit(df1).transform(df1)

    //Now let’s convert the categorical
    // columns into indexed columns so the model can interpret their impact
    // and fit them to the dataset

    val assembler1 = new VectorAssembler().
      setInputCols(Array( "YearsExperienceIndexed")).
      setOutputCol("features").
      transform(df1)

    val normalizer = new Normalizer().setInputCol("features").setOutputCol("normFeatures").setP(2.0).transform(assembler1)

    normalizer.show(false)

    val lr = new LinearRegression().setLabelCol("lebel").setFeaturesCol("normFeatures").setMaxIter(10).setRegParam(1.0).setElasticNetParam(1.0)

    //val lr = new LinearRegression().setLabelCol("lebel").setFeaturesCol("features")

    val Array(train, test) = normalizer.randomSplit(Array(0.7, 0.3))
    var lrModel=lr.fit(train)

    var lrPrediction= lrModel.transform(test).select("features","normFeatures", "lebel", "prediction").show()





  }

}

package ScalaFrameWorkForSpark.Project


import ScalaFrameWorkForSpark.SparkUtils.SparkConfig
import org.apache.spark.ml.feature.{Normalizer, StringIndexer, VectorAssembler}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression

object LinReg {
  def main(args: Array[String]): Unit = {

    val spark=SparkConfig.SparkSn
    import spark.sqlContext.implicits._

    val inputLines=spark.read.textFile("/Users/shuvamoymondal/Desktop/Salary_Data.csv")
    val header=inputLines.first()
    val csv_row=inputLines.filter(p=> p!=header)
    val data = csv_row.map(_.split(",")).map(p=> (Vectors.dense(p(0).toDouble),p(1).toDouble))

    val colname=Seq("features","label")
    val df= data.toDF(colname: _*)
    df.show(false)

     // .setMaxIter(10).setRegParam(1.0).setElasticNetParam(1.0)
    val lr = new LinearRegression().setMaxIter(10).setRegParam(1.0).setElasticNetParam(1.0)
    val trainTest = df.randomSplit(Array(0.7, 0.3))
    val train=trainTest(0)
    val test=trainTest(1)
    var lrModel=lr.fit(train)
    val prediction= lrModel.transform(test).cache()
    prediction.show(false)

    println(s"coeff: ${lrModel.coefficients} and intercept: ${lrModel.intercept}")
    val summary=lrModel.summary
    println($"numofIteration: ${summary.totalIterations}")
    //In regression analysis, the difference between the observed value of the dependent variable (y) and the predicted value (ŷ) is called the residual (e).
    summary.residuals.show(false)

    //Root Mean Square Error (RMSE) is the standard deviation of the residuals (prediction errors).
    // Residuals are a measure of how far from the regression line data points are;
    // RMSE is a measure of how spread out these residuals are.
    println($"RootMeanSquare: ${summary.rootMeanSquaredError}")





  }

}

package ScalaFrameWorkForSpark.Project


import ScalaFrameWorkForSpark.SparkUtils.SparkConfig
import org.apache.spark.ml.feature.{Normalizer, StringIndexer, VectorAssembler}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression

object MultifeaturesLinearReg {

  def main(args: Array[String]): Unit = {

    val spark=SparkConfig.SparkSn
    import spark.sqlContext.implicits._

    val inputLines=spark.read.textFile("/Users/shuvamoymondal/Desktop/50_Startups.csv")
    val header=inputLines.first()
    val csv_row=inputLines.filter(p=> p!=header)
    val csv_row1=csv_row.filter(p=> p!=0)
    val data = csv_row1.map(_.split(",")).map(p=> (Vectors.dense(p(0).toDouble,p(1).toDouble,p(2).toDouble),p(4).toDouble))

    val colname=Seq("features","label")
    val df= data.toDF(colname: _*)
    df.show(false)

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

  }
}

package ScalaFrameWorkForSpark.Project


import ScalaFrameWorkForSpark.SparkUtils.SparkConfig
import breeze.linalg.DenseVector
import org.apache.spark.ml.feature.{Normalizer, StringIndexer, VectorAssembler}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

object LogisticRegression {

  def main(args: Array[String]): Unit = {

    val spark=SparkConfig.SparkSn
    import spark.sqlContext.implicits._
    val inputLines=spark.read.textFile("/Users/shuvamoymondal/Desktop/Social_Network_Ads.csv")
    val header=inputLines.first()
    val csv_row=inputLines.filter(p=> p!=header)
    val data = csv_row.map(_.split(",")).map(p=> (Vectors.dense(p(2).toInt,p(3).toDouble),p(4).toInt))

    val colname=Seq("features","label")
    val df= data.toDF(colname: _*)
    df.show(false)

    val trainTest = df.randomSplit(Array(0.7, 0.3))
    val train=trainTest(0)
    val test=trainTest(1)

    val countTotal_train=train.count();
    println($"countTotal: ${countTotal_train}" )

    val countTotal_test=test.count();
    println($"countTotal: ${countTotal_test}" )

    val lr = new LogisticRegression().setMaxIter(10).setRegParam(1.0).setElasticNetParam(1.0)

    val lrModel= lr.fit(train)
    val prediction= lrModel.transform(test).cache()
    prediction.show(false)

    println(s"coeff: ${lrModel.coefficients} and intercept: ${lrModel.intercept}")
    val summary=lrModel.summary

    val evaluator= new BinaryClassificationEvaluator().setLabelCol("label").setRawPredictionCol("rawPrediction").setMetricName("areaUnderROC")
    val accuracy= evaluator.evaluate(prediction)
    println($"accuracy: ${accuracy}" )

    val countTotal=prediction.count();
    println($"countTotal: ${countTotal}" )

    val selectCol= prediction.select("label","prediction")
   val correct= selectCol.filter($"label" === $"prediction").count()
    println($"correct: ${correct}" )
    val wrong= selectCol.filter(!($"label" === $"prediction")).count()
    println($"wrong: ${wrong}" )

    val truePositive= selectCol.filter($"prediction"=== 0.0).filter($"label" === $"prediction").count()
    println($"truePositive: ${truePositive}" )

    val falseNegetive= selectCol.filter($"prediction"=== 1.0).filter($"label" === $"prediction").count()
    println($"falseNegetive: ${falseNegetive}" )


    val ratioWrong= wrong.toDouble/countTotal.toDouble
    println($"ratioWrong: ${ratioWrong}" )

    val ratioright= correct.toDouble/countTotal.toDouble
    println($"ratioright: ${ratioright}" )


  }

}

package ScalaFrameWorkForSpark.Project


import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{DecisionTreeClassifier, LogisticRegression}
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, MulticlassClassificationEvaluator, RegressionEvaluator}
import org.apache.spark.ml.feature._
import ScalaFrameWorkForSpark.SparkUtils.SparkConfig
import org.apache.spark.ml.regression.{DecisionTreeRegressor, GBTRegressor, RandomForestRegressor}
import org.apache.spark.sql.functions._

object DecisionTree {

  def main(args: Array[String]): Unit = {

    val spark=SparkConfig.SparkSn
    import spark.implicits._

    val data =spark.read.format("csv").option("header","true").option("inferSchema","true").load("/Users/shuvamoymondal/Desktop/UCI_Credit_Card.csv")

    val df = data.withColumn("ID",data.col("ID").cast("String")).withColumnRenamed("default.payment.next.month","Y")
    val numNegetive= df.filter(df("Y")=== 0).count()

    val dataSize= df.count()
    val ratio=(dataSize-numNegetive).toDouble/dataSize
    val df1= df.withColumn("classweightCol", when($"Y" === 0.0,ratio).otherwise(1.0 - ratio))

    val categorical= Array("SEX","MARRIAGE","AGE","EDUCATION","PAY_0","PAY_2","PAY_3","PAY_4","PAY_5","PAY_6")

    var transDF= df1
    for (c<- categorical) {
      var str1 = c + "_Index"
      var str2 = c + "_Vec"
      val indexer = new StringIndexer().setInputCol(c).setOutputCol(str1)
      val encoder = new OneHotEncoder().setInputCol(str1).setOutputCol(str2)
      val pipeline = new Pipeline().setStages(Array(indexer, encoder))
      transDF = pipeline.fit(transDF).transform(transDF)
    }
    transDF.show(2)

    val assembler= new VectorAssembler().setInputCols(Array("LIMIT_BAL","BILL_AMT1",
      "BILL_AMT2","BILL_AMT3","BILL_AMT4","BILL_AMT5","BILL_AMT6","PAY_AMT1","PAY_AMT2","PAY_AMT3"
      ,"PAY_AMT4","PAY_AMT5","PAY_AMT6","SEX_Vec","MARRIAGE_Vec","AGE_Vec","EDUCATION_Vec",
      "PAY_0_Vec","PAY_2_Vec","PAY_3_Vec","PAY_4_Vec","PAY_5_Vec","PAY_6_Vec")).setOutputCol("features")


        val pipeline= new Pipeline().setStages(Array(assembler))
        val dfful= pipeline.fit(transDF).transform(transDF)
      dfful.show(2)

     val splits= dfful.randomSplit(Array(0.8,0.2), seed=11L)
    val train= splits(0).cache()
    val test= splits(1)

    // Create two evaluator
    val binaryClassification= new BinaryClassificationEvaluator().setLabelCol("Y").setRawPredictionCol("rawPrediction")
    binaryClassification.setMetricName("areaUnderROC")
    val regressorEval= new RegressionEvaluator().setLabelCol("Y").setPredictionCol("prediction")
    regressorEval.setMetricName("rmse")

    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("Y").setPredictionCol("prediction").setMetricName("accuracy")


    val dt= new DecisionTreeRegressor().setLabelCol("Y").setFeaturesCol("features").setMaxBins(5).setMaxDepth(5)
    val pipeline1= new Pipeline().setStages(Array(dt))
    val modelDt= pipeline1.fit(train)
    val predictionDt= modelDt.transform(test)
    val rmsev=regressorEval.evaluate(predictionDt)
    println(rmsev)



    val lr = new LogisticRegression().setLabelCol("Y").setFeaturesCol("features").setMaxIter(10).setRegParam(1.0).setElasticNetParam(1.0)
    val pipeline2= new Pipeline().setStages(Array(lr))
    val lrModel= pipeline2.fit(train)
    val prediction= lrModel.transform(test).cache()
    val rmsevforLR=regressorEval.evaluate(prediction)
    println(rmsevforLR)


    val Rf= new RandomForestRegressor().setFeaturesCol("features").setLabelCol("Y")
    val pipeline3= new Pipeline().setStages(Array(Rf))
    val modelF= pipeline3.fit(train)
    val prediction2= modelF.transform(test).cache()
    val rmsevforRF=regressorEval.evaluate(prediction2)
    println(rmsevforRF)


    val Gb= new GBTRegressor().setLabelCol("Y").setFeaturesCol("features")
    val pipeline4= new Pipeline().setStages(Array(Gb))
    val Gbmodel= pipeline4.fit(train)
    val GbPredict= Gbmodel.transform(test)
    val rmsevforGb=regressorEval.evaluate(GbPredict)
    println(rmsevforGb)

    val accuracy = evaluator.evaluate(GbPredict)
    println("Test Error = " + (1.0 - accuracy))


    val dtcls= new DecisionTreeClassifier().setFeaturesCol("features").setLabelCol("Y").setMaxBins(5).setMaxDepth(5)
    val pipeline6= new Pipeline().setStages(Array(dtcls))
    val modelDtC= pipeline6.fit(train)
    val predictionDtC= modelDtC.transform(test)
    val rmsevC=regressorEval.evaluate(predictionDtC)




  }
}

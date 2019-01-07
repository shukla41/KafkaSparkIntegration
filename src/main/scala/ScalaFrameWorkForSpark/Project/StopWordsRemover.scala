package ScalaFrameWorkForSpark.Project

import ScalaFrameWorkForSpark.SparkUtils.SparkConfig
import org.apache.spark.ml.feature.{StopWordsRemover, StringIndexer, VectorAssembler, VectorIndexer}
import java.util.Arrays

import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.linalg.Vectors



object StopWordsRemover {

  def main(args: Array[String]): Unit = {

    val spark=SparkConfig.SparkSn

    val remover = new StopWordsRemover()
      .setInputCol("raw")
      .setOutputCol("filtered")

    val dataSet = spark.createDataFrame(Seq(
      (0, Seq("I", "saw", "the", "red", "balloon")),
      (1, Seq("Mary", "had", "a", "little", "lamb"))
    )).toDF("id", "raw")

    remover.transform(dataSet).show(false)

    /////////////////////////////////////////


    val df = spark.createDataFrame(Seq(
      (6, "M"),
      (1, "M"),
      (2, "M"),
      (3, "F"),
      (4, "F"),
      (5, "M")
    )).toDF("id", "category")

    val indexers = new StringIndexer()
      .setInputCol("category")
      .setOutputCol("categoryIndex")
      .fit(df)
    val indexed = indexers.transform(df)

    indexed.show(false)

    val assembler = new VectorAssembler()
      .setInputCols(Array("id","categoryIndex"))
      .setOutputCol("features")

    val output = assembler.transform(indexed)
    output.select("features").show(false)

    /////////////////////////////////////////////


    val indexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexed")
      .setMaxCategories(10)

    val indexerModel = indexer.fit(output)
    val indexedData = indexerModel.transform(output)
    indexedData.show()

/////////////////////////////////////////////////

  }
}

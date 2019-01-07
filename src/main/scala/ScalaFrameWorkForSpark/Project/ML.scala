package ScalaFrameWorkForSpark.Project


import ScalaFrameWorkForSpark.SparkUtils.SparkConfig
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row

object ML {

  def main(args: Array[String]): Unit = {


    val spark=SparkConfig.SparkSn


    import spark.sqlContext.implicits._

    val df = spark.sparkContext.parallelize( Seq(
      (0, "one flesh one bone one true religion"),
      (1, "all flesh is grass"),
      (2, "one is all all is one"))).toDF("label", "sentence")


    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    val wordsData = tokenizer.transform(df)

    val hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(20)

    println('${hashingTF.getOutputCol})
    val featurizedData = hashingTF.transform(wordsData)

    //featurizedData.show(false)
    // alternatively, CountVectorizer can also be used to get term frequency vectors

    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
    val idfModel = idf.fit(featurizedData)

    val rescaledData = idfModel.transform(featurizedData)
    rescaledData.select("label", "features").show(false)

    import scala.collection.JavaConversions._

    for (row <- rescaledData.collectAsList) {
      println(row)
    }

    /*val df1=df.explode(col("ID"), col("document")) {case row: Row =>
      val id = row(0).asInstanceOf[Int]
      val words = row(1).asInstanceOf[String].split(",")
      words.map(word => (id,word))
    } */



  }
}

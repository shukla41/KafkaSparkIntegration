package ScalaFrameWorkForSpark.SparkUtils

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
  * Created by shuvamoymondal on 7/22/18.
  */
object SparkConfig {

  def StreamingSession : StreamingContext ={
    val conf = new SparkConf().setMaster("local[2]").setAppName("Kafka10")

    //Read messages in batch of 30 seconds
    val sparkStreamingContext = new StreamingContext(conf, Durations.seconds(30))
    sparkStreamingContext

  }

  def SparkSn: SparkSession = {

    val spark: SparkSession =
      SparkSession
        .builder()
        .appName("AppName")
        .config("spark.master", "local")
        .getOrCreate()

    spark
  }

  def sparkContext: SparkContext = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    sc
  }
}

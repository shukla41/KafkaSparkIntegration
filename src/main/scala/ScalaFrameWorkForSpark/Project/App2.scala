package ScalaFrameWorkForSpark.Project


import ScalaFrameWorkForSpark.KafkaUtils.KafkaProducer
import ScalaFrameWorkForSpark.SparkUtils._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Durations, StreamingContext}
/**
  * Created by shuvamoymondal on 7/21/18.
  */


/**
  * Created by shuvamoymondal on 7/20/18.
  */
object App2 {

  def main(args: Array[String]): Unit = {

    val StrmingCntxt=SparkConfig.StreamingSession
    val spark=SparkConfig.SparkSn
    KafkaSparkStreamingIntegration.KafkaSparkMessageReaderToSave(StrmingCntxt,spark)
    //KafkaProducer.KafkaProducerJob("json_data", "/usr/local/src/json_file")
    StrmingCntxt.start()
    StrmingCntxt.awaitTermination()

  }

  }



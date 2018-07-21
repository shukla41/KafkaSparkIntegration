package ScalaFrameWorkForSpark.Project

/**
  * Created by shuvamoymondal on 7/21/18.
  */

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by shuvamoymondal on 7/20/18.
  */
object App2 {

  def main(args: Array[String]): Unit = {


    val groupId ="something"
    val brokers="localhost:2181"
    val numThreads=10
    val zkQuorum="localhost:2181"

    /* if (args.length < 4) {
       System.err.println("Usage: KafkaWordCount <zkQuorum><group> <topics> <numThreads>")
       System.exit(1)
     } */

    // val Array(zkQuorum, group, topics, numThreads) = args
    val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    //ssc.checkpoint("checkpoint")

    val kafkaParams = Map(
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "auto.offset.reset" -> "earliest",
      "group.id" -> "tweet-consumer"
    )

    val Topic= List("test3").toSet
    //val topics = Array("topicA", "topicB")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](Topic, kafkaParams)
    )

    stream.map(record => (record.key, record.value))
    // Get the lines, split them into words, count the words and print
    stream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}


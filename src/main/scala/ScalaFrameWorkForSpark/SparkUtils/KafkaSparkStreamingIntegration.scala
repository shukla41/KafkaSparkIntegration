package ScalaFrameWorkForSpark.SparkUtils


import com.databricks.spark.xml.XmlRelation
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Durations, Seconds, StreamingContext}
import ScalaFrameWorkForSpark.SparkUtils._
import org.apache.spark
import ScalaFrameWorkForSpark.Common._

import scala.collection.mutable



/**
  * Created by shuvamoymondal on 7/22/18.
  */
object KafkaSparkStreamingIntegration {



  def KafkaSparkMessageReaderToSave(StreamCotxt : StreamingContext,spark: SparkSession) = {

    val kafkaParam = new mutable.HashMap[String, String]()
    kafkaParam.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    kafkaParam.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaParam.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaParam.put(ConsumerConfig.GROUP_ID_CONFIG, "group1")
    kafkaParam.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
    kafkaParam.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")


    //Configure Spark to listen messages in topic test
    val topicList = List("test4")

    // Read value of each message from Kafka and return it
    val messageStream = KafkaUtils.createDirectStream(StreamCotxt,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicList, kafkaParam))

    FileDirectoryHandling.cmd("mkdir /usr/local/src/file2")

    val lines = messageStream.map(consumerRecord => consumerRecord.value().asInstanceOf[String])
    //lines.map(p=> p.foreach(println)
    lines.saveAsTextFiles("/usr/local/src/file2/f.xml")

    messageStream.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      val result = ParsingXmlToDataframe(spark)
    }
  }
    //def ParsingXmlToDataframe(messages: RDD[ConsumerRecord[String, String]]) = {

    def ParsingXmlToDataframe(spark: SparkSession) = {

      val df = spark.sqlContext.read.format("com.databricks.spark.xml")
        .option("rowTag", "Employee").load("/usr/local/src/file2/f.xml-[0-5]*")

      df.show()
      FileDirectoryHandling.cmd("rm -rf /usr/local/src/file2")

    }



}


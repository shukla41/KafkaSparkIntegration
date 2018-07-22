package ScalaFrameWorkForSpark.Project

/**
  * Created by shuvamoymondal on 7/21/18.
  */

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

import scala.collection.mutable
import com.databricks.spark.xml.util.XmlFile

import com.databricks.spark.xml._

import com.databricks.spark.xml.XmlReader
import com.databricks.spark.xml.parsers.StaxXmlParser
/**
  * Created by shuvamoymondal on 7/20/18.
  */
object App2 {

  def process(msg :RDD[String]) = {

    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()





  }


  def processLogs(messages: RDD[ConsumerRecord[String, String]]) = {


    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()


    val p=messages.map(_.value)
    //println(p.foreach(k=>println("The val",k)))
    val df1 = new XmlReader().xmlRdd(spark.sqlContext, p)

    val df = spark.sqlContext.read.format("com.databricks.spark.xml")
      .option("rowTag", "Employee").load("/usr/local/src/file2/f.xml-[0-5]*")


    df.show()

  }


  def main(args: Array[String]): Unit = {

    val kafkaParam = new mutable.HashMap[String, String]()
    kafkaParam.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    kafkaParam.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaParam.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaParam.put(ConsumerConfig.GROUP_ID_CONFIG, "group1")
    kafkaParam.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
    kafkaParam.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")

    val conf = new SparkConf().setMaster("local[2]").setAppName("Kafka10")

    //Read messages in batch of 30 seconds
    val sparkStreamingContext = new StreamingContext(conf, Durations.seconds(30))

    //Configure Spark to listen messages in topic test
    val topicList = List("test4")

    // Read value of each message from Kafka and return it
    val messageStream = KafkaUtils.createDirectStream(sparkStreamingContext,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicList, kafkaParam))

    val lines = messageStream.map(consumerRecord => consumerRecord.value().asInstanceOf[String])
    lines.saveAsTextFiles("/usr/local/src/file2/f.xml")

    messageStream.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      val result = processLogs(rdd)



    }
    sparkStreamingContext.start()
    sparkStreamingContext.awaitTermination()
  }
}


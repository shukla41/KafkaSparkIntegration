package ScalaFrameWorkForSpark.Project

/**
  * Created by shuvamoymondal on 7/21/18.
  */

import com.databricks.spark.xml.XmlRelation
import org.apache.kafka.clients.consumer.ConsumerConfig
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
import scala.xml.XML
import com.databricks.spark.xml.XmlReader
/**
  * Created by shuvamoymondal on 7/20/18.
  */
object App2 {

  def process(msg :String) = {

    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    //val g=msg.map(p=>p)

        val xmlStringRDD = spark.sparkContext.parallelize(Seq(msg))
        println(xmlStringRDD.getClass)

        val df = new XmlReader().xmlRdd(spark.sqlContext, xmlStringRDD)


    //val df=spark.sqlContext.read.format("com.databricks.spark.xml").option("rowTag", "Employee").
    println(df.count())
    df.show()
    //val rowRdd = msg.map(p=> p)
    println("I am haere")



    //println(rowRdd)


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
    val topicList = List("test3")

    // Read value of each message from Kafka and return it
    val messageStream = KafkaUtils.createDirectStream(sparkStreamingContext,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicList, kafkaParam))

    val lines = messageStream.map(consumerRecord => consumerRecord.value().asInstanceOf[String])

    lines.foreachRDD(x=> x.foreach(process))
    //val words = lines.map(line=> line.split(",")).map(word=> word)
    //println(words.getClass)

    /*lines.foreachRDD(r => {
      println("*** got an RDD, size = " + r.count())
      r.foreach(s => println(s))
      if (r.count() > 0) {
        // let's see how many partitions the resulting RDD has -- notice that it has nothing
        // to do with the number of partitions in the RDD used to publish the data (4), nor
        // the number of partitions of the topic (which also happens to be four.)
        println("*** " + r.getNumPartitions + " partitions")
        r.glom().foreach(a => println("*** partition size = " + a.size))
      }
    })

   // lines.print()
    // Break every message into words and return list of words
    val words = lines.flatMap(_.split(" "))

    // Take every word and return Tuple with (word,1)
    val wordMap = words.map(word => (word, 1))

    // Count occurance of each word
    val wordCount = wordMap.reduceByKey((first, second) => first + second)

   // wordCount.print()
   */

    sparkStreamingContext.start()
    sparkStreamingContext.awaitTermination()
  }
}


package ScalaFrameWorkForSpark.KafkaUtils

import java.util.Properties


import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source


/**
  * Created by shuvamoymondal on 7/20/18.
  */
object KafkaProducer {


  var lines = ""

  def KafkaConfigurationSetUp = {
    val props = new Properties()

    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    producer
  }


  def KafkaProducerJob(topic_name: String, Path: String) = {
    val topic = topic_name
    val files = ReadFileFromDirectory.getListOfFiles(Path)
    for (name <- files) {
      println("filename", name)
      lines = Source.fromFile(name.toString).getLines.mkString("\n")
      ReadFileFromDirectory.delete(name)
      val str = (1 to 1).map(x => lines).mkString
      val message = new ProducerRecord[String, String](topic, null, str)
      KafkaProducer.KafkaConfigurationSetUp.send(message)


    }
  }

    def KafkaJsonProducerJob(topic_name: String, str: String) = {
      val topic = topic_name
        val message = new ProducerRecord[String, String](topic, null, str)
        KafkaProducer.KafkaConfigurationSetUp.send(message)


      }



}

package ScalaFrameWorkForSpark.KafkaUtils

/**
  * Created by shuvamoymondal on 7/23/18.
  */

import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import scala.collection.JavaConversions._

object KafkaConsumer {

  def KafkaConsumerConfigurationSetUp = {
    val consumerProperties = new Properties()
    consumerProperties.put("bootstrap.servers", "localhost:9092")
    consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")


    val consumer = new KafkaConsumer[String, String](consumerProperties)
    consumer
  }

  def KafkaconsumerJob(topic_name: String, Path: String) = {

   KafkaConsumer.KafkaConsumerConfigurationSetUp.subscribe(Collections.singletonList(topic_name))
    val records = KafkaConsumer.KafkaConsumerConfigurationSetUp.poll(100)
    for (record: ConsumerRecord[String, String] <- records) {

      println("key " + record.key())
      println("value " + record.value())
    }


  }

}
package ScalaFrameWorkForSpark.Project

import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import ScalaFrameWorkForSpark.KafkaUtils.ReadFileFromDirectory._
import scala.collection.JavaConversions._
/**
  * Created by shuvamoymondal on 7/24/18.
  */
object test {

  def main(args: Array[String]): Unit = {
    val consumerProperties = new Properties()
    consumerProperties.put("bootstrap.servers", "localhost:9092")
    consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    consumerProperties.put("group.id", "dummy-group")

    val consumer = new KafkaConsumer[String, String](consumerProperties)
    consumer.subscribe(Collections.singletonList("json_data"))

    while(true) {
      val records = consumer.poll(100)

      for(record:ConsumerRecord[String, String] <- records) {

        println(manOf(record.value()))
        println("value " + record.value())


        Thread.sleep(10000)
      }
    }

  }
}

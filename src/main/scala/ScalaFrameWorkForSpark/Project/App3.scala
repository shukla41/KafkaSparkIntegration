package ScalaFrameWorkForSpark.Project

import java.util.{Collections, Properties}
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import scala.collection.JavaConversions._

/**
  * Created by shuvamoymondal on 7/23/18.
  */
object App3 {
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

        println("key " + record.key())
        println("value " + record.value())

        Thread.sleep(10000)
      }
    }
  }
}
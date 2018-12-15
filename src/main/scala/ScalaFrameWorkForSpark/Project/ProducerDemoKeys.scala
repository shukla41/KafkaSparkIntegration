package ScalaFrameWorkForSpark.Project


import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import java.util.Properties
import org.apache.log4j.{Level,Logger}

object ProducerDemoKeys {

  def main(args: Array[String]): Unit = {
    val bootstrap="127.0.0.1:9092"
    val LOG: Logger = Logger.getLogger("APP")
    LOG.setLevel(Level.INFO)
    Logger.getLogger("org").setLevel(Level.ERROR)


    val properties= new Properties()
    properties.put("bootstrap.servers", bootstrap)
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    // create the producer
    val producer = new KafkaProducer[String, String](properties)

    for(i <- 1 to 10) {
      val topic = "test1"
      val value = "helloworld" + Integer.toString(i)
      val key = "Key_" + Integer.toString(i)
      val prodRcd = new ProducerRecord[String, String](topic, key, value)

      LOG.info("Key: " + key)


      producer.send(prodRcd, new Callback {
        override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {

          if (e == null) {

            LOG.info("Received topic:" + recordMetadata.topic())
            LOG.info("partition:" + recordMetadata.partition())
            LOG.info("Offset:" + recordMetadata.offset())
            LOG.info("Timestamp:" + recordMetadata.timestamp())

          }

          else {
            LOG.error("Error while Producing", e)
          }

        }
      }).get() // to make it Synchronous. Block the send(). But dont do this in prod
    }
    producer.flush()
    producer.close()
  }

}

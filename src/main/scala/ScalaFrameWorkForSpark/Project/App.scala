package ScalaFrameWorkForSpark.Project

/**
 * Hello world!
 *
 */


import ScalaFrameWorkForSpark.KafkaUtils._
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import java.util.Properties
import org.apache.log4j.{Level,Logger}



object App {
  def main(args: Array[String]): Unit = {


    //KafkaProducer.KafkaConfigurationSetUp
    //while (true) {


      //KafkaProducer.KafkaProducerJob("test4", "/usr/local/src/file")
       //Thread.sleep(10000)
     // KafkaConsumer.KafkaconsumerJob("json_data")
      //println(KafkaconsumerJob("json_data"))
      //Thread.sleep(10000)
    // KafkaProducer.KafkaProducerJob("json_data", "/usr/local/src/json_file")
      //Thread.sleep(10000)

      //KafkaConsumer.KafkaconsumerJob("json_data", "/usr/local/src/json_file")
      //Thread.sleep(10000)

    //}


    val bootstrap="127.0.0.1:9092"
    val LOG: Logger = Logger.getLogger("APP")
    LOG.setLevel(Level.INFO)
    Logger.getLogger("org").setLevel(Level.ERROR)


    // Create Producer Properties
    val properties= new Properties()
    properties.put("bootstrap.servers", bootstrap)
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    // create the producer
    val producer = new KafkaProducer[String, String](properties)

    // Create the producer Record

    val prodRcd= new ProducerRecord[String,String]("test","helloworld")

    // Synchronous process as we are waiting for the acknowledgement for every message
    // It will slow down your process, use it when we only care about error for every message

    try {
      val meta = producer.send(prodRcd).get()
      LOG.info("Received topic:" + meta.partition())
      LOG.info("partition:" + meta.partition())
      LOG.info("Offset:" + meta.offset())
      LOG.info("Timestamp:" + meta.timestamp())
    }
    catch
      {
        case e: Exception =>  LOG.error("Error while Producing",e)
      }
    finally {
      producer.flush()

    }


    // Asynchronous process as we send a message and provide a callback function to recceive acknowledgement
    // We dont wait for success and failure . It is in beetween Fire and Forget and Synchronous one.
    // It gives better through put.
    // Also note that in asynchronous approach the number of messages which are "in fligh"
    // is controlled by max.in.flight.requests.per.connection parameter.

    producer.send(prodRcd,new Callback {
      override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {

        if (e == null) {

          LOG.info("Received topic:" + recordMetadata.topic())
          LOG.info("partition:" + recordMetadata.partition())
          LOG.info("Offset:" + recordMetadata.offset())
          LOG.info("Timestamp:" + recordMetadata.timestamp())

        }

        else {
          LOG.error("Error while Producing",e)
        }

      }
    })
    producer.flush()
    producer.close()

  }
}

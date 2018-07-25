package ScalaFrameWorkForSpark.Project

/**
 * Hello world!
 *
 */


import ScalaFrameWorkForSpark.KafkaUtils._

object App {
  def main(args: Array[String]): Unit = {


    //KafkaProducer.KafkaConfigurationSetUp
    while (true) {
      KafkaProducer.KafkaProducerJob("test4", "/usr/local/src/file")
       Thread.sleep(10000)
     // KafkaConsumer.KafkaconsumerJob("json_data")
      //println(KafkaconsumerJob("json_data"))
      //Thread.sleep(10000)
    // KafkaProducer.KafkaProducerJob("json_data", "/usr/local/src/json_file")
      //Thread.sleep(10000)

      //KafkaConsumer.KafkaconsumerJob("json_data", "/usr/local/src/json_file")
      //Thread.sleep(10000)

    }
  }
}

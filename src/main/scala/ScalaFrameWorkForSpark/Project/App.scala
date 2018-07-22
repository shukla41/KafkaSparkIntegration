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

    }
  }
}

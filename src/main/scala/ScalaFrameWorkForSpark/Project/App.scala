package ScalaFrameWorkForSpark.Project

/**
 * Hello world!
 *
 */

import ScalaFrameWorkForSpark.KafkaUtils

object App {
  def main(args: Array[String]): Unit = {

    KafkaUtils.KafkaProducer.KafkaConfigurationSetUp
    while (true) {
      KafkaUtils.KafkaProducer.KafkaProducerJob("test3", "/usr/local/src/file")
      Thread.sleep(10000)
    }
  }
}

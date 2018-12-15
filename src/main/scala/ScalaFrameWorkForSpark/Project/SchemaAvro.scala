package ScalaFrameWorkForSpark.Project

import ScalaFrameWorkForSpark.KafkaUtils.AvroProducer
import org.apache.log4j.BasicConfigurator
import org.slf4j.LoggerFactory


object SchemaAvro {

  def main(args: Array[String]): Unit = {

    BasicConfigurator.configure()
    val logger = LoggerFactory.getLogger(getClass)
    logger.info("Starting the application")

    val producer = new AvroProducer
    producer.start


  }
}

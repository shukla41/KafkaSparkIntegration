package ScalaFrameWorkForSpark


/**
 * Hello world!
 *
 */

import java.util.Properties
import ReadFile.ReadFileFromDirectory
import org.apache.kafka.clients.producer._
import scala.io.Source
import scala.util.Random
import java.io.File
import ReadFile._

object App {
  def main(args: Array[String]): Unit = {

    ReadFile.KafkaProducer.KafkaConfigurationSetUp
    while (true) {
      ReadFile.KafkaProducer.KafkaProducerJob("test3", "/usr/local/src/file")
      Thread.sleep(10000)
    }
  }
}

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

object App {
  def main(args: Array[String]): Unit = {


    val props = new Properties()

    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")


    val topic = "test3"
    val messagesPerSec = 1
    val wordsPerMessage = 15
    var lines =""
    val random = new Random()

    //val names = Seq("aaaa", "bbbbb", "cccccc", "ddddd", "eeeee", "fffff", "zz", "yy", "cc", "pp", "oo", "uu", "ss", "ll");


    //print(lines)
    /*for(i<- 1 to 5){
      val record = new ProducerRecord(TOPIC, "key", s"hello $i")
      println("record is" +record)
      producer.send(record)
    }*/

    val producer = new KafkaProducer[String, String](props)

    while (true) {


      val files = ReadFileFromDirectory.getListOfFiles("/usr/local/src/file")
      for (name <- files) {
        lines = Source.fromFile(name.toString).getLines.mkString
        ReadFileFromDirectory.delete(name)


        //(1 to 1).foreach { messageNum =>

        val str = (1 to 1).map(x => lines).mkString(" ")

        println(str)

        val message = new ProducerRecord[String, String](topic, null, str)

        producer.send(message)

        // }
        lines = ""
        Thread.sleep(10000)
      }
    }


  }
}

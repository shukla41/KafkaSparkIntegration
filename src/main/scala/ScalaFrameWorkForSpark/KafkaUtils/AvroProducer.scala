package ScalaFrameWorkForSpark.KafkaUtils

import java.util.Properties

import org.apache.avro.Schema.Parser
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.log4j.{Level, Logger}
import io.confluent.kafka.serializers.KafkaAvroSerializer

case class User(name: String, favoriteNumber: Int, favoriteColor: String)


class AvroProducer {

  val bootstrap="127.0.0.1:9092"
  val LOG: Logger = Logger.getLogger("APP")
  LOG.setLevel(Level.INFO)
  Logger.getLogger("org").setLevel(Level.ERROR)


  // Create Producer Properties
  val properties= new Properties()
  properties.put("bootstrap.servers", bootstrap)
  properties.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")
  properties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")
  properties.put("schema.registry.url", "http://127.0.0.1:8081")
  properties.put("acks", "1")

  val producer = new KafkaProducer[String, GenericData.Record](properties)
  val schemaParser = new Parser

  val key = "key1"
  val valueSchemaJson =
    s"""
    {
      "namespace": "com.avro.junkie",
      "type": "record",
      "name": "User2",
      "fields": [
        {"name": "name", "type": "string"},
        {"name": "favoriteNumber",  "type": "int"},
        {"name": "favoriteColor", "type": "string"}
      ]
    }
  """
  val valueSchemaAvro = schemaParser.parse(valueSchemaJson)
  val avroRecord = new GenericData.Record(valueSchemaAvro)

  val mary = new User("Mary", 840, "Green")
  avroRecord.put("name", mary.name)
  avroRecord.put("favoriteNumber", mary.favoriteNumber)
  avroRecord.put("favoriteColor", mary.favoriteColor)

  def start = {
    try {
      val record = new ProducerRecord("test1", key, avroRecord)
      val ack = producer.send(record).get()
      // grabbing the ack and logging for visibility
      LOG.info(s"${ack.toString} written to partition ${ack.partition.toString}")
    }
    catch {
      case e: Throwable => LOG.error(e.getMessage, e)
    }
  }


}

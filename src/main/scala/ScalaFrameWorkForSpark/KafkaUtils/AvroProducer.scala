package ScalaFrameWorkForSpark.KafkaUtils


//to set compatibility
//curl -X PUT -H "Content-Type: application/json" --data '{"compatibility": "NONE"}' http://localhost:8081/config/{test3}-value

import java.util.Properties

import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.log4j.{Level, Logger}



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
      "namespace": "com.avro.shuva",
      "type": "record",
      "name": "User3",
      "fields": [

        {"name": "favoriteNumber",  "type": "int"}

      ]
    }
  """
  val valueSchemaAvro = schemaParser.parse(valueSchemaJson)
  //val valueSchemaAvro = new Schema.Parser().parse(valueSchemaJson)
  val avroRecord = new GenericData.Record(valueSchemaAvro)


  val mary = new User(840)
 // avroRecord.put("name", mary.name)
  avroRecord.put("favoriteNumber", mary.favoriteNumber)
//  avroRecord.put("favoriteColor", mary.favoriteColor)

  def start = {
    try {
      val record = new ProducerRecord("test3", key, avroRecord)
      val ack = producer.send(record).get()
      // grabbing the ack and logging for visibility
      LOG.info(s"${ack.toString} written to partition ${ack.partition.toString}")
      producer.flush()
      producer.close()
    }
    catch {
      case e: Throwable => LOG.error(e.getMessage, e)
    }
  }



}

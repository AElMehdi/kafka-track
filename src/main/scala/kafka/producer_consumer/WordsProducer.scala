package kafka.producer_consumer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object WordsProducer {

  def main(args: Array[String]): Unit = {
    val producerProperties = new Properties()
    producerProperties.put("bootstrap.servers", "localhost:9092")
    producerProperties.put("key.serializer", classOf[StringSerializer])
    producerProperties.put("value.serializer", classOf[StringSerializer])

    val str = scala.io.StdIn.readLine()

    writeToKafka("quick-start", producerProperties, str)
  }

  def writeToKafka(topic: String, config: Properties, content: String): Unit = {
    val producer = new KafkaProducer[String, String](config)
    val record = new ProducerRecord[String, String](topic, "key", content)
    producer.send(record)
    producer.close()
  }
}

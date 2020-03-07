package kafka.producer_consumer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object WordsProducer {

  def main(args: Array[String]): Unit = {
    val kafkaProducer = createKafkaProducer()
    val prompting = true
    while (prompting) {
      writeToProducer(kafkaProducer, "streams-plaintext-input", scala.io.StdIn.readLine())
    }

  }

  def createKafkaProducer(): KafkaProducer[String, String] = {
    val config = new Properties()
    config.put("bootstrap.servers", "localhost:9092")
    config.put("key.serializer", classOf[StringSerializer])
    config.put("value.serializer", classOf[StringSerializer])
    new KafkaProducer[String, String](config)
  }

  def writeToProducer(kafkaProducer: KafkaProducer[String, String], topic: String, message: String) = {
    val record = new ProducerRecord[String, String](topic, message)
    kafkaProducer.send(record)
    kafkaProducer.flush()
  }
}

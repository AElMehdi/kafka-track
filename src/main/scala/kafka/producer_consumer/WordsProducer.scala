package kafka.producer_consumer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object WordsProducer {

   def writeToKafka(topic: String, config: Properties, content: String): Unit = {
      val producer = new KafkaProducer[String, String](config)
      val record = new ProducerRecord[String, String](topic, "key", content)
      producer.send(record)
      producer.close()
   }
}

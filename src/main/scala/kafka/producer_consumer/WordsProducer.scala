package kafka.producer_consumer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object WordsProducer {

   def main(args: Array[String]): Unit = {
     val kafkaProducer = createKafkaProducer()

      var str = ""
      val prompting = true
      while (prompting) {
         str = scala.io.StdIn.readLine()
         writeToProducer(kafkaProducer, "quick-start")
      }

   }

   def createKafkaProducer(): KafkaProducer = {
      val config = new Properties()
      config.put("bootstrap.servers", "localhost:9092")
      config.put("key.serializer", classOf[StringSerializer])
      config.put("value.serializer", classOf[StringSerializer])
      new KafkaProducer[String, String](config)
   }

   def writeToProducer(topic: String, message: String): Unit = {
      val record = new ProducerRecord[String, String](topic, message)
      producer.send(record)
      producer.close()
   }
}

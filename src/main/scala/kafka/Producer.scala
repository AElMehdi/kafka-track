import java.util.Properties

import org.apache.kafka.clients.producer._

class Producer {
  def main(args: Array[String]): Unit = {
    writeToKafka("streams-plaintext-input", new Properties(), "A message coming from the producer.")
  }

  def writeToKafka(topic: String, config: Properties, content: String): Unit = {
    val props = config
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "key", content)
    producer.send(record)
    producer.close()
  }
}

package kafka.producer_consumer

import java.time.Duration.ofSeconds
import java.util
import java.util.Properties
import collection.JavaConverters._
import org.apache.kafka.clients.consumer.KafkaConsumer

object Consumer {
  def main(args: Array[String]): Unit = {
    consumeFromKafka("quick-start")
  }

  def consumeFromKafka(topic: String) = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "latest")
    props.put("group.id", "consumer-group")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Arrays.asList(topic))

    while (true) {
      val record = consumer.poll(ofSeconds(1)).asScala
      for (data <- record.iterator)
        println("What's going on?" + data.value())
    }
  }
}

package kafka.producer_consumer

import java.time.Duration.ofSeconds
import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._

object Consumer {
  def main(args: Array[String]): Unit = {
    consumeFromKafka("streams-wordcount-output")
  }

  def consumeFromKafka(topic: String) = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer")
    props.put("print.key", "true")
    props.put("formatter", "kafka.tools.DefaultMessageFormatter")
    props.put("auto.offset.reset", "earliest")
    props.put("group.id", "consumer-group")

    val consumer: KafkaConsumer[String, Long] = new KafkaConsumer[String, Long](props)
    consumer.subscribe(util.Arrays.asList(topic))

    while (true) {
      val record = consumer.poll(ofSeconds(1)).asScala
      for (data <- record.iterator)
        println("Word: " + data.key() + ", appeared: " + data.value())
    }
  }
}

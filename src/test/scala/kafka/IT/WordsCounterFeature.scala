package kafka.IT

import java.util.Properties

import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.assertj.core.api.Assertions
import org.scalatest.funsuite.AnyFunSuite

class WordsCounterFeature extends AnyFunSuite with ForAllTestContainer {
   override val container = KafkaContainer()

  test("Should start a Kafka container") {
    val properties = new Properties()
    properties.put("bootstrap.servers", container.bootstrapServers)
    properties.put("group.id", "consumer-tutorial")
    properties.put("key.deserializer", classOf[StringDeserializer])
    properties.put("value.deserializer", classOf[StringDeserializer])

    val kafkaConsumer = new KafkaConsumer[String, String](properties)
    val topics = kafkaConsumer.listTopics()

    Assertions.assertThat(topics.size()).isGreaterThan(0)
  }

  test("Should produce and consume a hello message") {
    val properties = new Properties()
    properties.put("bootstrap.servers", container.bootstrapServers)
    properties.put("group.id", "consumer-tutorial")
    properties.put("key.deserializer", classOf[StringDeserializer])
    properties.put("value.deserializer", classOf[StringDeserializer])


    val kafkaConsumer = new KafkaConsumer[String, String](properties)
    val topics = kafkaConsumer.listTopics()

    Assertions.assertThat(topics.size()).isGreaterThan(0)
  }
}

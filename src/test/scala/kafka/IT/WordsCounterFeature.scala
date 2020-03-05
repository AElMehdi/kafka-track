package kafka.IT

import java.time.Duration
import java.util.{Collections, Properties}

import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import kafka.producer_consumer.WordsProducer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
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
    val producerProperties = new Properties()
    producerProperties.put("bootstrap.servers", container.bootstrapServers)
    producerProperties.put("key.serializer", classOf[StringSerializer])
    producerProperties.put("value.serializer", classOf[StringSerializer])


    val consumerProperties = new Properties()
    consumerProperties.put("bootstrap.servers", container.bootstrapServers)
    consumerProperties.put("group.id", "consumer-tutorial")
    consumerProperties.put("key.deserializer", classOf[StringDeserializer])
    consumerProperties.put("value.deserializer", classOf[StringDeserializer])


    WordsProducer.writeToKafka("input-topic", producerProperties, "Hello Kafka")



    val kafkaConsumer = new KafkaConsumer[String, String](consumerProperties)
    kafkaConsumer.subscribe(Collections.singletonList("input-topic"))
    val records = kafkaConsumer.poll(Duration.ofSeconds(1))

    Assertions.assertThat(records.count()).isGreaterThan(0)
  }

  test("Should create an input topic") {
    val producerProperties = new Properties()
    producerProperties.put("bootstrap.servers", container.bootstrapServers)
    producerProperties.put("key.serializer", classOf[StringSerializer])
    producerProperties.put("value.serializer", classOf[StringSerializer])


    val consumerProperties = new Properties()
    consumerProperties.put("bootstrap.servers", container.bootstrapServers)
    consumerProperties.put("group.id", "consumer-tutorial")
    consumerProperties.put("key.deserializer", classOf[StringDeserializer])
    consumerProperties.put("value.deserializer", classOf[StringDeserializer])


    WordsProducer.writeToKafka("input-topic", producerProperties, "Hello Kafka")



    val kafkaConsumer = new KafkaConsumer[String, String](consumerProperties)
    kafkaConsumer.subscribe(Collections.singletonList("input-topic"))

    Assertions.assertThat(kafkaConsumer.listTopics().get("input-topic")).isNotNull
  }
}

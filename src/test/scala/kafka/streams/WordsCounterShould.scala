package kafka.streams

import java.util.{Properties, Random}

import org.apache.kafka.common.serialization.{LongDeserializer, LongSerializer, StringDeserializer, StringSerializer}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.{KeyValue, StreamsConfig, TopologyTestDriver}
import org.assertj.core.api.Assertions.assertThat
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.JavaConverters._

class WordsCounterShould extends AnyFunSuite {
  var streamsConfig: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-scala-integration-test")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy config")
    // Use a temporary directory for storing state, which will be automatically removed after the test.
    p.put(StreamsConfig.STATE_DIR_CONFIG, "store")
    p
  }

  private val outputTopic = "output-topic"
  private val inputTopic = "input-topic"


  test("Count numbers") {
    val inputTextLines = Seq(
      "Hello kafka Streams",
      "All streams lead to kafka",
      "Join Kafka Summit",
      "Finally it's working!",
    )

    val expected = Map(("hello", 1L),
      ("all", 1L),
      ("streams", 2L),
      ("lead", 1L),
      ("to", 1L),
      ("join", 1L),
      ("kafka", 3L),
      ("summit", 1L),
      ("finally", 1L),
      ("it", 1L),
      ("s", 1L),
      ("working", 1L))

    val builder = new StreamsBuilder

    // Create a stream from the "input-topic"
    val textLines: KStream[Array[Byte], String] = builder.stream[Array[Byte], String](inputTopic)

    // Counting logic applied to the stream
    val wordCounts: KTable[String, Long] = textLines
      .flatMapValues(value => value.toLowerCase.split("\\W+"))
      .groupBy((_, word) => word)
      .count()

    // Convert the KTable to a KStream and materialize it to a topic
    wordCounts.toStream.to(outputTopic)


    val topologyTestDriver = new TopologyTestDriver(builder.build(), streamsConfig)


    try {
      // Setup input and output topics
      // TODO: Use a VoidSerializer
      val input = topologyTestDriver.createInputTopic(inputTopic,
        new LongSerializer,
        new StringSerializer)
      val output = topologyTestDriver.createOutputTopic(outputTopic,
        new StringDeserializer,
        new LongDeserializer)

      // Publish the input topics
      // TODO: A null key should be used instead of a random long.
      val l = new Random().nextLong()
      input.pipeKeyValueList(inputTextLines.map(v => new KeyValue(java.lang.Long.valueOf(l), v)).asJava)
      assertThat(output.readKeyValuesToMap()).isEqualTo(expected.asJava)
    } finally {
      topologyTestDriver.close()
    }

  }
}

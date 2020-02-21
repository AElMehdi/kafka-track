package kafka.streams

import java.util.Properties

import org.apache.kafka.common.serialization.{LongDeserializer, StringDeserializer, StringSerializer}
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.{KeyValue, StreamsConfig, TopologyTestDriver}
import org.assertj.core.api.Assertions.assertThat
import org.scalatest.funsuite.AnyFunSuite

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
    )

    val expected = Map(("hello", 1L),
      ("all", 1L),
      ("streams", 2L),
      ("lead", 1L),
      ("to", 1L),
      ("join", 1L),
      ("kafka", 3L),
      ("summit", 1L))

    val builder = new StreamsBuilder

    val textLines: KStream[Array[Byte], String] = builder.stream[Array[Byte], String](inputTopic)

    val wordCounts: KTable[String, Long] = textLines
      .flatMapValues(value => value.toLowerCase.split("\\W+"))
      .groupBy((_, word) => word)
      .count()

    wordCounts.toStream.to(outputTopic)


    val topologyTestDriver = new TopologyTestDriver(builder.build(), streamsConfig)


    val input = topologyTestDriver.createInputTopic(inputTopic,
       null,
      new StringSerializer)
    val output = topologyTestDriver.createOutputTopic(outputTopic,
      new StringDeserializer,
      new LongDeserializer)

    input.pipeKeyValueList(inputTextLines.map(v => new KeyValue(null, v)).asJava)

    assertThat(output.readKeyValuesToMap()).isEqualTo(expected)
  }
}

package kafka.streams

import java.time.Duration

import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable, Materialized}

object WordsCounter extends App {
  // 1. Add kafka broker configuration
  // 2. Build a Kafka Stream
  // 3. Process/Enrich data as it flows

  private val builder = new StreamsBuilder
  private val textLines: KStream[String, String] = builder
    .stream[String, String]("textLinesTopic")

  private val wordCounts: KTable[String, Long] = textLines
    .flatMapValues(textLine => textLine.toLowerCase().split("\\w+"))
    .groupBy((_, word) => word)
    .count()(Materialized.as("counts-store"))

  wordCounts.toStream.to("WordsWithCountsTopic")

  var props = ???

  private val streams = new KafkaStreams(builder.build(), props)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }
}

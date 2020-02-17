package kafka.streams

import java.time.Duration
import java.util.Properties

import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.Serdes._

object WordsCounter extends App {
  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p
  }

  private val streamsBuilder = new StreamsBuilder
  private val textLines: KStream[String, String] = streamsBuilder
    .stream[String, String]("textLinesTopic")

  private val wordCounts: KTable[String, Long] = textLines
    .flatMapValues(textLine => textLine.toLowerCase().split("\\w+"))
    .groupBy((_, word) => word)
    .count()(Materialized.as("counts-store"))

  wordCounts.toStream.to("WordsWithCountsTopic")

  private val streams = new KafkaStreams(streamsBuilder.build(), props)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }
}

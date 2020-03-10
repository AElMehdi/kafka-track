package kafka.streams

import java.time.Duration
import java.util.Properties

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.kstream.{Consumed, KStream, KTable, Produced}
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object WordsCounter extends App {
   val props: Properties = {
      val p = new Properties()
      p.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application")
      p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      p
   }

  private val stringSerde: Serde[String] = Serdes.String
  private val longSerde: Serde[Long] = Serdes.Long

  private val streamsBuilder = new StreamsBuilder
  private val textLines: KStream[String, String] = streamsBuilder
        .stream("streams-plaintext-input")( Consumed.`with`(stringSerde, stringSerde))

   private val wordCounts: KTable[String, Long] = textLines
         .flatMapValues(textLine => textLine.toLowerCase().split("\\w+"))
         .groupBy((_, word) => word)
         .count()
//   (Materialized.as("counts-store"))

   wordCounts.toStream.to("streams-wordcount-output")(Produced.`with`(stringSerde, longSerde))

   private val streams = new KafkaStreams(streamsBuilder.build(), props)
   streams.start()

   sys.ShutdownHookThread {
      streams.close(Duration.ofSeconds(10))
   }
}

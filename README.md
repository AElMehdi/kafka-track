# kafka-track

##Examples:
###Classic producer/consumer:
###Streaming:
Create input and output topics:
```commandline
kafka-topics --create --topic streams-plaintext-input --zookeeper zoo1:2181 --partitions 1 --replication-factor 1
kafka-topics --create --topic streams-wordcount-output --zookeeper zoo1:2181 --partitions 1 --replication-factor 1
```

Start a producer: 
```commandline
kafka-console-producer --broker-list localhost:9092 --topic streams-plaintext-input
```


Start a consumer: 
```commandline
kafka-console-consumer --topic streams-wordcount-output --from-beginning \
                                                  --bootstrap-server localhost:9092 \
                                                  --property print.key=true \
                                                  --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```


package com.bits.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerExample {

	private final static String TOPIC = "TestKafka";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

	private static Consumer<Long, String> createConsumer() {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		// Create the consumer using props.

		final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
		// Subscribe to the topic.

		consumer.subscribe(Collections.singletonList(TOPIC));

		return consumer;
	}

	public static List<String> runConsumer() throws InterruptedException {

		final Consumer<Long, String> consumer = createConsumer();
		final int giveUp = 100;
		int noRecordsCount = 0;

		
		List<String> messages = new ArrayList<String>();
		final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);

		consumerRecords.forEach(record -> {
			String message = record.value();
			messages.add(message);
		});

		consumer.commitAsync();

		consumer.close();
		System.out.println("DONE");
		return messages;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			runConsumer();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

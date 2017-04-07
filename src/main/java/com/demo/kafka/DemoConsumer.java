package com.demo.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class DemoConsumer implements Runnable {

	private final KafkaConsumer<String, String> consumer;
	private final String topic;
	private LongAdder adder;
	private AtomicBoolean stopConsumer;
	private LongAdder threadLocalAdder = new LongAdder();;

	public DemoConsumer(String brokers, String groupId, String topic,
			LongAdder adder, AtomicBoolean stopConsumer) {
		this.consumer = createConsumer(brokers, groupId);
		this.topic = topic;
		this.consumer.subscribe(Arrays.asList(this.topic));
		this.adder = adder;
		this.stopConsumer = stopConsumer;
	}

	private static KafkaConsumer createConsumer(String brokers, String groupId) {
		Properties props = new Properties();
		props.put("bootstrap.servers", brokers);
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		return new KafkaConsumer<>(props);
	}

	@Override
	public void run() {
		threadLocalAdder.add(0);
		while (stopConsumer.get()) {
			ConsumerRecords<String, String> records = consumer.poll(100);

			records.partitions().forEach(
					partition -> {

						records.records(partition)
								.parallelStream()
								.forEach(
										record -> {
											System.out.println(" fetched: "
													+ record.value()
													+ ", partition: "
													+ record.partition()
													+ ", offset: "
													+ record.offset()
													+ ", threadID: "
													+ Thread.currentThread()
															.getId());
											adder.increment();
											threadLocalAdder.increment();
										});
					});

			/*
			 * records.forEach(record ->{
			 * System.out.println("Regular --  fetched: " + record.value() +
			 * ", partition: " + record.partition() + ", offset: " +
			 * record.offset() + ", threadID: " +
			 * Thread.currentThread().getId()); });
			 */

		}
		System.out.println("**** Total  messages consumed  ****"
				+ threadLocalAdder.sum() + " by "
				+ Thread.currentThread().getId());

	}

}

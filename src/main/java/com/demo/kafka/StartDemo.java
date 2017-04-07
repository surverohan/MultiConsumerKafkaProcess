package com.demo.kafka;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

/*
 * This program will start single producer and multiple consumer one for each partition
 * 
 */
public class StartDemo {

	public static void main(String[] args) {
		LongAdder adder = new LongAdder();
		adder.add(0);
		AtomicBoolean stopConsumer = new AtomicBoolean(Boolean.TRUE);

		String brokersList = "localhost:9092";
		String groupId = "group01";
		String topic = "MultiConsumerKafkaTopic";
		int numberOfConsumer = 2;

		// Start Producer Thread
		new Thread(new DemoProducer(brokersList, topic)).start();
		// Start Consumer Thread
		for (int i = 0; i < numberOfConsumer; i++) {
			new Thread(new DemoConsumer(brokersList, groupId, topic, adder,
					stopConsumer)).start();

		}

		try {
			while (adder.sum() != 10000) {
				System.out.println("current  message processed " + adder.sum());
				Thread.sleep(100);

			}
			System.out.println(" Total  message processed " + adder.sum());

			stopConsumer.set(false);

		} catch (InterruptedException ie) {

		}
	}
}

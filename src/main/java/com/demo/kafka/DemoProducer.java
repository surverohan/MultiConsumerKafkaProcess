package com.demo.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class DemoProducer implements Runnable {

	private KafkaProducer<String, String> producer;
	private final String topic;

	public DemoProducer(String brokersVal, String topic) {
		this.topic = topic;
		this.producer = createKafkaProducer(brokersVal);
	}

	public static KafkaProducer createKafkaProducer(String brokersVal) {
		Properties props = new Properties();
		props.put("bootstrap.servers", brokersVal);
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");

		return new KafkaProducer<String, String>(props);
	}

	@Override
	public void run() {
		for (int i = 0; i < 10000; i++) {
			String msg = "Demo Message " + i;
			producer.send(new ProducerRecord<String, String>(topic, msg),
					new Callback() {
						public void onCompletion(RecordMetadata metadata,
								Exception e) {
							if (e != null) {
								System.out.println("Error for :" + msg
										+ " details " + e.getMessage());
							}
							System.out.println("Sent:" + msg + ", partition: "
									+ metadata.partition() + ", offset: "
									+ metadata.offset() + ", threadID: "
									+ Thread.currentThread().getId());
						}
					});

		}
		System.out.println("**** All messages Send ****");
		producer.close();

	}
}

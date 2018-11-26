package org.apache.commons.autoconfig.commonconfig.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MQConfirmCallback mqConfirmCallback;


	public MQSender() {

	}

	public void send(Object message)  {

		log.info("send message:"+message);

		rabbitTemplate.setConfirmCallback(mqConfirmCallback);

		rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE, message);






	}




	/*public void sendTopic(Object message) {
		String msg = "";
		log.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}

	public void sendFanout(Object message) {
		String msg = "";
		log.info("send fanout message:"+msg);
		amqpTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", msg);
	}

	public void sendHeader(Object message) {
		String msg = "";
		log.info("send fanout message:"+msg);
		MessageProperties properties = new MessageProperties();
		properties.setHeader("header1", "value1");
		properties.setHeader("header2", "value2");
		Message obj = new Message(msg.getBytes(), properties);
		amqpTemplate.convertAndSend(RabbitMQConfig.HEADERS_EXCHANGE, "", obj);
	}*/
}

package org.apache.commons.autoconfig.commonconfig.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


public class MQReceiver {

	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);



	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void receive(String message) {
		log.info("receive message:"+message);
	}

		/*@RabbitListener(queues = RabbitMQConfig.TOPIC_QUEUE1)
		public void receiveTopic1(String message) {
			log.info(" topic  queue1 message:"+message);
		}

		@RabbitListener(queues = RabbitMQConfig.TOPIC_QUEUE2)
		public void receiveTopic2(String message) {
			log.info(" topic  queue2 message:"+message);
		}

		@RabbitListener(queues = RabbitMQConfig.HEADER_QUEUE)
		public void receiveHeaderQueue(byte[] message) {
			log.info(" header  queue message:"+new String(message));
		}
*/

}

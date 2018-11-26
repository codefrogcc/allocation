package org.apache.commons.autoconfig.commonconfig.config.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

@Component
public class MQConfirmCallback implements RabbitTemplate.ConfirmCallback {



    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("发送消息:"+b);


    }
}

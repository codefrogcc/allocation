package org.apache.commons.autoconfig.commonconfig.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

//@Component
public class RabbitTemplateConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{

    private Logger logger = LoggerFactory.getLogger(RabbitTemplateConfig.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override//消息发送确认
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("消息唯一标识："+correlationData);
        logger.info("确认结果："+ack);
        logger.info("失败原因："+cause);
    }

    @Override//消息接收确认
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("消息主体 message:"+message);
        logger.info("消息主体 message:"+replyCode);
        logger.info("描述："+replyText);
        logger.info("消息使用的交换机："+exchange);
        logger.info("消息使用的");
    }
}

package org.apache.commons.autoconfig.commonconfig.config.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RabbitListener(queues = RabbitMQConfig.QUEUE)
public class TestMQReceiver {

    @RabbitHandler
    public void process(String msg, Channel channel, Message message) throws IOException {
        System.out.println("HelloReceiver收到  : " + msg +"收到时间"+new Date());
             try {

                 Thread.sleep(10000);
                 channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

             System.out.println("receiver success");
             } catch (Exception e) {
               e.printStackTrace();
                 channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
                 System.out.println("receiver fail");
             }


    }

}

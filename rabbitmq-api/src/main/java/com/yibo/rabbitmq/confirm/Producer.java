package com.yibo.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 16:50
 * @Description:
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1、创建一个ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2、通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3、通过Connection创建一个Channel
        Channel channel = connection.createChannel();

        //4、指定消息的投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        //5、发送消息
        String msg = "hello rabbitmq send confirm message!";
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes("UTF-8"));

        //6、添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 返回成功
             * @param deliveryTag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------ack----------");
            }

            /**
             * 返回失败
             * @param deliveryTag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------no ack----------");
            }
        });
    }
}

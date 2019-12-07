package com.yibo.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/2 23:49
 * @Description:
 */
public class TopicExchangeProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1、创建一个ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        //2、通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3、通过Connection创建一个Channel
        Channel channel = connection.createChannel();

        //4 声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.save";
        String routingKey2 = "user.update";
        String routingKey3 = "user.delete.abc";

        //4、通过Channel发送数据
        String msg = "Hello World RabbitMQ for Topic Exchange Message";
        //1 exchange   2 routingKey
        channel.basicPublish(exchangeName,routingKey1,null,msg.getBytes("UTF-8"));
        channel.basicPublish(exchangeName,routingKey2,null,msg.getBytes("UTF-8"));
        channel.basicPublish(exchangeName,routingKey3,null,msg.getBytes("UTF-8"));

        //5、关闭相关连接
        channel.close();
        connection.close();
    }
}

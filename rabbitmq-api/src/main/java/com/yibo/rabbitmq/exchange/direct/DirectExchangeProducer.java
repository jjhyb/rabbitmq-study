package com.yibo.rabbitmq.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/2 22:38
 * @Description:
 */
public class DirectExchangeProducer {

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

        //4 声明
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct111";

        //4、通过Channel发送数据
        String msg = "Hello World RabbitMQ for Direct Exchange Message";
        //1 exchange   2 routingKey
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes("UTF-8"));

        //5、关闭相关连接
        channel.close();
        connection.close();
    }
}
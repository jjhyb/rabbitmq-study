package com.yibo.rabbitmq.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/2 23:50
 * @Description:
 */
public class TopicExchangeConsumer {

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
        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        String routingKey = "user.#";

        //表示声明了一个交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        //表示声明了一个队列
        channel.queueDeclare(queueName, false, false, false, null);
        //建立一个绑定关系:
        channel.queueBind(queueName, exchangeName, routingKey);

        //5、创建消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(envelope.getRoutingKey() + "：" + message);
            }
        };

        //参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(queueName,true,consumer);
    }
}

package com.yibo.rabbitmq.message;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 1:14
 * @Description:
 */
public class Consumer {

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

        //4、声明(创建)一个队列
        String queueName = "test001";
        channel.queueDeclare(queueName,true,false,false,null);

        //5、创建消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("RoutingKey="+envelope.getRoutingKey() + "，message=" + message);
                Map<String, Object> headers = properties.getHeaders();
                System.out.println("properties的属性：headers="+headers);
            }
        };

        //6、设置channel
        channel.basicConsume(queueName,true,consumer);
    }
}

package com.yibo.rabbitmq.limit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 17:42
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

        String exchangeName = "test_qos_exchange";
        String queueName = "test_qos_queue";
        String routingKey = "qos.#";

        //4、声明交换机和队列，然后进行绑定设置，指定路由key
        channel.exchangeDeclare(exchangeName,"topic",true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);


        //5、创建消费者
        DefaultConsumer consumer = new MyConsumer(channel);

        //6、限流方式 AutoAck设置为false
        channel.basicConsume(queueName,false,consumer);
        //
        channel.basicQos(0,1,false);
    }
}

package com.yibo.rabbitmq.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        //这是一个普通的交换机、队列、路由
        String exchangeName = "test_dlx_exchange";
        String queueName = "test_dlx_queue";
        String routingKey = "dlx.#";

        //4、声明交换机和队列，然后进行绑定设置，指定路由key
        channel.exchangeDeclare(exchangeName,"topic",true);
        Map<String,Object> agruments = new HashMap<>();
        agruments.put("x-dead-letter-exchange", "dlx.exchange");
        //这个agruments属性要声明到队列上
        channel.queueDeclare(queueName,true,false,false,agruments);
        channel.queueBind(queueName,exchangeName,routingKey);

        //死信队列的声明
        channel.exchangeDeclare("dlx.exchange","topic",true,false,null);
        channel.queueDeclare("dlx.queue",true,false,false,null);
        channel.queueBind("dlx.queue","dlx.exchange","#");

        //5、创建消费者
        DefaultConsumer consumer = new MyConsumer(channel);

        //6、手动签收 AutoAck设置为false
        channel.basicConsume("dlx.queue",false,consumer);
    }
}

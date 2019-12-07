package com.yibo.rabbitmq.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 1:14
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

        Map<String,Object> map = new HashMap<>();
        map.put("将故事写成我们",111);
        map.put("夜曲",222);

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .headers(map)
                .build();

        //4、通过Channel发送数据
        String msg = "hello rabbitmq!";
        //1 exchange   2 routingKey
        channel.basicPublish("","test001",properties,msg.getBytes("UTF-8"));

        //5、关闭相关连接
        channel.close();
        connection.close();
    }
}

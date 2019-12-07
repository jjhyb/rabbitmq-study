package com.yibo.rabbitmq.returnlistener;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 17:42
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

        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "error.save";

        //5、发送消息
        String msg = "hello rabbitmq send return message!";
//        channel.basicPublish(exchangeName,routingKey,true,null,msg.getBytes("UTF-8"));

        //第三个参数mandatory=true,意味着路由不到的话mq也不会删除消息,false则会自动删除
        channel.basicPublish(exchangeName,routingKeyError,true,null,msg.getBytes("UTF-8"));
//        channel.basicPublish(exchangeName,routingKeyError,false,null,msg.getBytes("UTF-8"));

        //6、添加一个return监听
        channel.addReturnListener(new ReturnListener() {

            @Override
            public void handleReturn(int replyCode, String replyText, String exchange,
                                     String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("----------handle return----------");
                System.out.println("replyCode：" + replyCode);
                System.out.println("replyText：" + replyText);
                System.out.println("exchange：" + exchange);
                System.out.println("routingKey：" + routingKey);
                System.out.println("properties：" + properties);
                System.out.println("body：" + new String(body,"UTF-8"));
            }
        });
    }
}

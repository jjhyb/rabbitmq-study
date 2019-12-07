package com.yibo.rabbitmq.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Map;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 18:33
 * @Description:
 */
public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(envelope.getRoutingKey() + "：" + message);

        Map<String, Object> headers = properties.getHeaders();
        Integer num = (Integer) headers.get("num");

        if(num == 0){
            //第三个参数，true：重回队列，false：不重回队列
            //重回队列，会将消息重新添加到消息的尾部
            channel.basicNack(envelope.getDeliveryTag(),false,true);
            return;
        }

        //手动签收
        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}

package com.yibo.rabbitmq;

import com.yibo.rabbitmq.entity.Order;
import com.yibo.rabbitmq.producer.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: huangyibo
 * @Date: 2019/12/5 1:10
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void testSender1(){
        Map<String,Object> properties = new HashMap<>();
        properties.put("number","123456");
        properties.put("sendTime",LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        rabbitSender.send("hello rabbitmq",properties);
    }

    @Test
    public void testSender2(){
        Order order = new Order();
        order.setId("1");
        order.setName("订单");
        rabbitSender.sendOrder(order);
    }
}

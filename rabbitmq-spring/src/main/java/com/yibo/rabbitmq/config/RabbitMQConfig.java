package com.yibo.rabbitmq.config;

import com.yibo.rabbitmq.adapter.MessageDelegate;
import com.yibo.rabbitmq.messageconverter.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 23:10
 * @Description:
 */

@Configuration
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 针对消费者配置
     * 1、 设置交换机类型
     * 2、 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     *
     */
    @Bean
    public TopicExchange exchange001() {

        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {

        return new Queue("queue001", true); //队列持久
    }

    @Bean
    public Binding binding001() {

        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {

        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {

        return new Queue("queue002", true); //队列持久
    }

    @Bean
    public Binding binding002() {

        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {

        return new Queue("queue003", true); //队列持久
    }

    @Bean
    public Queue queue_image() {

        return new Queue("image_queue", true); //队列持久
    }

    @Bean
    public Queue queue_pdf() {

        return new Queue("pdf_queue", true); //队列持久
    }

    @Bean
    public Binding binding003() {
        //同一个Exchange绑定了2个队列
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //添加多个队列进行监听
        container.setQueues(queue001(),queue002(),queue003(),queue_image(),queue_pdf());
        //当前消费者数量
        container.setConcurrentConsumers(1);
        //最大消费者数量
        container.setMaxConcurrentConsumers(5);
        //设置重回队列，一般设置false
        container.setDefaultRequeueRejected(false);
        //设置自动签收机制
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //设置listener外露
        container.setExposeListenerChannel(true);
        //消费端标签生成策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                //每个消费端都有自己独立的标签
                return queue + "_" + UUID.randomUUID().toString();
            }
        });

        //消息监听
        /*container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody());
                System.err.println("----------消费者: " + msg);
            }
        });*/


        /*
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        adapter.setMessageConverter(new TextMessageConverter());
        container.setMessageListener(adapter);
        */


        /**
         * 适配器方式: 我们的队列名称 和 方法名称 也可以进行一一的匹配
         */
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setMessageConverter(new TextMessageConverter());
        Map<String, String> queueOrTagToMethodName = new HashMap<>();
        queueOrTagToMethodName.put("queue001", "method1");
        queueOrTagToMethodName.put("queue002", "method2");
        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        container.setMessageListener(adapter);
        return container;
    }
}

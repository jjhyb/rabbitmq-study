package com.yibo.rabbitmq.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/12/5 15:35
 * @Description:
 *
 * 这里的Processor接口是定义来作为后面类的参数，这一接口定义来通道类型和通道名称。
 * 通道名称是作为配置用，通道类型则决定了app会使用这一通道进行发送消息还是从中接收消息。
 *
 */

@Component
public interface Processor {

    //String INPUT_CHANNEL = "input_channel";
    String OUTPUT_CHANNEL = "output_channel";

    //注解@Output声明了它是一个输出类型的通道，名字是output_channel。这一名字与app1中通道名一致，
    // 表明注入了一个名字为output_channel的通道，类型是output，发布的主题名为mydest。
    @Output(Processor.OUTPUT_CHANNEL)
    MessageChannel logoutput();
}

package com.yibo.rabbitmq.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangyibo
 * @Date: 2019/12/5 2:49
 * @Description:
 */

@Data
public class Order implements Serializable {

    private String id;

    private String name;
}

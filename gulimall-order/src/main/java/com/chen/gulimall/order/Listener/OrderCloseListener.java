package com.chen.gulimall.order.Listener;

import com.chen.gulimall.order.TO.SeckillOrderTo;
import com.chen.gulimall.order.entity.OrderEntity;
import com.chen.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/19 11:15
 * @description
 */
@RabbitListener(queues = "order.release.order.queue")
@Service
public class OrderCloseListener {
    @Autowired
    OrderService orderService;
    @RabbitHandler
    public void listener(SeckillOrderTo order, Channel channel, Message message) throws IOException {

//        getDeliveryTag 获取消息的唯一标识符
        try {
            orderService.CreateSeckillOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}

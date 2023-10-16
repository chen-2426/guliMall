package com.chen.gulimall.order.service.impl;

import com.chen.gulimall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.order.dao.OrderItemDao;
import com.chen.gulimall.order.entity.OrderItemEntity;
import com.chen.gulimall.order.service.OrderItemService;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * Message message, //原生详细信息
     * T<发送的消息类型> OrderReturnReasonEntity content,
     * Channel ,当前传输数据的通道
     * @param message
     * @param content
     *
     * 特性：
     * 1 多个订单服务；同一个订单只有一个服务能收到
     * 2.只有一个订单处理完，才会接收下一个订单
     *
     *
     *  @RabbitListener 表在类和方法上
     *  @RabbitHandler 标在方法上 --》用于重载RabbitListener(表在类上），方便接收不同类型消息
     */
    @RabbitListener(queues = {"java-queue"})

    public void recieveMessage(Message message, //原生详细信息
                               OrderReturnReasonEntity content,
                               Channel channel) throws InterruptedException {
        byte[] body = message.getBody();
        Thread.sleep(30 );
        MessageProperties messageProperties = message.getMessageProperties();//获取消息属性

    }


}
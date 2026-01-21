package com.hongs.skyserver.task;


import com.hongs.skyserver.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void processPayTimeOutOrder(){
        log.info("定时处理支付超时订单-当前时间: {}", LocalDateTime.now());
        orderService.processPayTimeOutOrder();
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void processDeliveryTimeOutOrder(){
        log.info("定时处理派送超时订单-当前时间: {}", LocalDateTime.now());
        orderService.processDeliveryTimeOutOrder();
    }
}

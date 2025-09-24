package com.sky.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    // @Scheduled(cron = "1/5 * * * * ?")
    public void processTimeoutOrder() {
        log.info("定时自动处理超时订单:{}", LocalDateTime.now());
        List<Orders> byStatusAndOrderTimeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,LocalDateTime.now().minusMinutes(15));

        if (byStatusAndOrderTimeLT != null && byStatusAndOrderTimeLT.size() > 0) {
            for (Orders orders : byStatusAndOrderTimeLT) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    // @Scheduled(cron = "0/5 * * * * ?")
    public void processDeliveryOrder() {
        log.info("每日定时清除正在派送的订单:{}", LocalDateTime.now());
        List<Orders> byStatusAndOrderTimeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,LocalDateTime.now().minusMinutes(60));
        if (byStatusAndOrderTimeLT != null && byStatusAndOrderTimeLT.size() > 0) {
            for (Orders orders : byStatusAndOrderTimeLT) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }

    }
}

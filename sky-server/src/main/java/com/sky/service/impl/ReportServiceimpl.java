package com.sky.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;

@Service
public class ReportServiceimpl implements ReportService{

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        // 添加日期
        while(!begin.isAfter(end)) {
            begin = begin.plusDays(1);;
            dateList.add(begin);
        }

        // 每天的营业额
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date,LocalTime.MAX);
            HashMap hashMap = new HashMap();
            hashMap.put("begin", beginTime);
            hashMap.put("end", endTime);
            hashMap.put("status", Orders.COMPLETED);
            Double turnOver = orderMapper.sumByMap(hashMap);
            turnOver = (turnOver == null) ? 0.0 : turnOver;
            turnoverList.add(turnOver);

        }


        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

}

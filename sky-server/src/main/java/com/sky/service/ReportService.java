package com.sky.service;

import java.time.LocalDate;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

public interface ReportService {

    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO getUserReportVO(LocalDate begin,LocalDate end);
}

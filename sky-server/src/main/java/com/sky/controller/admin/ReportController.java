package com.sky.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(tags = "统计相关接口")
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
        // @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate begin,
        // @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate end 这会解析成"年-分钟-日"
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("营业额数据统计:{},{}",begin,end);
        TurnoverReportVO turnoverStatistics = reportService.getTurnoverStatistics(begin,end);
        return Result.success(turnoverStatistics);
    }
}

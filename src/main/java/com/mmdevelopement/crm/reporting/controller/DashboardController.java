package com.mmdevelopement.crm.reporting.controller;

import com.mmdevelopement.crm.reporting.entity.AggregatedStatistics;

import com.mmdevelopement.crm.reporting.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/api/v1/reporting/dashboard")
@RequiredArgsConstructor
public class DashboardController {


    private final StatisticsService statisticsService;


    @GetMapping("/")
    public AggregatedStatistics getDashboardData(@RequestParam Date fromDate,
                                                 @RequestParam Date toDate) {

        return statisticsService.getDashboardData(fromDate, toDate);
    }
}

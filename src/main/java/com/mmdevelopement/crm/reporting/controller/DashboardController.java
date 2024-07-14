package com.mmdevelopement.crm.reporting.controller;

import com.mmdevelopement.crm.reporting.entity.AggregatedStatistics;

import com.mmdevelopement.crm.reporting.entity.PartnerStatistics;
import com.mmdevelopement.crm.reporting.entity.PartnerStatisticsRequest;
import com.mmdevelopement.crm.reporting.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/")
    public PartnerStatistics getPartnerStatistics(@RequestBody PartnerStatisticsRequest request) {
        return statisticsService.getPartnerStatistics(request);
    }
}

package com.mmdevelopement.crm.reporting.controller;

import com.mmdevelopement.crm.reporting.entity.AggregatedStatistics;

import com.mmdevelopement.crm.reporting.entity.PartnerStatistics;
import com.mmdevelopement.crm.reporting.entity.PartnerStatisticsRequest;
import com.mmdevelopement.crm.reporting.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public AggregatedStatistics getDashboardData(@RequestParam(name = "fromDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
                                                @RequestParam(name = "toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
        return statisticsService.getDashboardData(fromDate, toDate);
    }

    @GetMapping("/partner/{partnerId}")
    public PartnerStatistics getPartnerStatistics( @PathVariable(name = "partnerId") Integer partnerId,
                                                   @RequestParam(name = "fromDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
                                                   @RequestParam(name = "toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
        return statisticsService.getPartnerStatistics(new PartnerStatisticsRequest(fromDate, toDate, partnerId));
    }
}

package com.mmdevelopement.crm.reporting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PartnerStatisticsRequest {
    private Date fromDate;
    private Date toDate;
    private Integer partnerId;
}

package com.mmdevelopement.crm.reporting.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PartnerStatisticsRequest {

    private Date fromDate;
    private Date toDate;
    private Integer partnerId;

}

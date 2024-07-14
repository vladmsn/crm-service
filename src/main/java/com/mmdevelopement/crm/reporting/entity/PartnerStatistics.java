package com.mmdevelopement.crm.reporting.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerStatistics {
    private String partnerName;
    private float totalReceived;
    private float totalPayed;
}

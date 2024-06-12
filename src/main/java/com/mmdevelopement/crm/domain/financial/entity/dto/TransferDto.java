package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.accounts.TransferEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TransferDto {

    private Integer id;

    private Double amount;

    private Integer fromBankAccountId;
    private String fromBankAccountName;

    private Integer toBankAccountId;
    private String toBankAccountName;

    private Date date;

    private String description;

    private String paymentMethod;

    private String reference;

    public static TransferDto fromEntity(TransferEntity entity) {
        return new TransferDto()
                .setId(entity.id())
                .setAmount(entity.amount())
                .setFromBankAccountId(entity.fromBankAccountId())
                .setToBankAccountId(entity.toBankAccountId())
                .setDate(entity.transferDate())
                .setDescription(entity.description())
                .setPaymentMethod(entity.paymentMethod())
                .setReference(entity.reference());
    }

    public TransferEntity toEntity() {
        return new TransferEntity()
                .id(getId())
                .amount(getAmount())
                .fromBankAccountId(getFromBankAccountId())
                .toBankAccountId(getToBankAccountId())
                .transferDate(getDate())
                .description(getDescription())
                .paymentMethod(getPaymentMethod())
                .reference(getReference());
    }

}

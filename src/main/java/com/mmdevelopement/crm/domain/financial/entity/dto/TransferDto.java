package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.accounts.TransferEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true, fluent = true)
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
                .id(entity.id())
                .amount(entity.amount())
                .fromBankAccountId(entity.fromBankAccountId())
                .toBankAccountId(entity.toBankAccountId())
                .date(entity.transferDate())
                .description(entity.description())
                .paymentMethod(entity.paymentMethod())
                .reference(entity.reference());
    }

    public TransferEntity toEntity() {
        return new TransferEntity()
                .id(id())
                .amount(amount())
                .fromBankAccountId(fromBankAccountId())
                .toBankAccountId(toBankAccountId())
                .transferDate(date())
                .description(description())
                .paymentMethod(paymentMethod())
                .reference(reference());
    }

}

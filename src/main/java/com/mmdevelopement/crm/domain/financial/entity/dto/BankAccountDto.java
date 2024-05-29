package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.accounts.BankAccountEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class BankAccountDto {

        private Integer id;

        private String name;

        private String accountNumber;

        private String description;

        private Double sold;

        public static BankAccountDto fromEntity(BankAccountEntity entity) {
            return new BankAccountDto()
                    .id(entity.id())
                    .name(entity.name())
                    .accountNumber(entity.accountNumber())
                    .description(entity.description())
                    .sold(entity.sold());
        }

        public BankAccountEntity toEntity() {
            return new BankAccountEntity()
                    .id(id())
                    .name(name())
                    .accountNumber(accountNumber())
                    .description(description())
                    .sold(sold());
        }
}

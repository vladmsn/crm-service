package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.accounts.BankAccountEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BankAccountDto {

        private Integer id;

        private String name;

        private String accountNumber;

        private String description;

        private Double sold;

        public static BankAccountDto fromEntity(BankAccountEntity entity) {
            return new BankAccountDto()
                    .setId(entity.id())
                    .setName(entity.name())
                    .setAccountNumber(entity.accountNumber())
                    .setDescription(entity.description())
                    .setSold(entity.sold());
        }

        public BankAccountEntity toEntity() {
            return new BankAccountEntity()
                    .id(getId())
                    .name(getName())
                    .accountNumber(getAccountNumber())
                    .description(getDescription())
                    .sold(getSold());
        }
}

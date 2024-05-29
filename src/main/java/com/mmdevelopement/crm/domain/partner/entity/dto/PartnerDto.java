package com.mmdevelopement.crm.domain.partner.entity.dto;

import com.mmdevelopement.crm.domain.partner.entity.PartnerEntity;
import com.mmdevelopement.crm.utils.ImageUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class PartnerDto {

    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private String website;
    private String reference;
    private String CUI;
    private String address;
    private String city;
    private String county;
    private String country;
    private String postalCode;
    private String image;
    private Boolean saved;

    public static PartnerDto fromEntity(PartnerEntity partnerEntity) {
        return new PartnerDto()
                .id(partnerEntity.id())
                .name(partnerEntity.name())
                .email(partnerEntity.email())
                .phoneNumber(partnerEntity.phoneNumber())
                .website(partnerEntity.website())
                .reference(partnerEntity.reference())
                .city(partnerEntity.CUI())
                .address(partnerEntity.address())
                .city(partnerEntity.city())
                .county(partnerEntity.county())
                .country(partnerEntity.country())
                .postalCode(partnerEntity.postalCode())
                .image(partnerEntity.image() != null ? ImageUtils.encodeImage(partnerEntity.image()) : null)
                .saved(partnerEntity.saved());
    }

    public PartnerEntity toEntity() {
        return new PartnerEntity()
                .id(id)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .website(website)
                .reference(reference)
                .CUI(CUI)
                .address(address)
                .city(city)
                .county(county)
                .country(country)
                .postalCode(postalCode)
                .image(image != null ? ImageUtils.decodeImage(image) : null)
                .saved(saved);
    }
}

package com.mmdevelopement.crm.domain.partner.entity.dto;

import com.mmdevelopement.crm.domain.partner.entity.PartnerEntity;
import com.mmdevelopement.crm.utils.ImageUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
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
                .setId(partnerEntity.id())
                .setImage(partnerEntity.name())
                .setEmail(partnerEntity.email())
                .setPhoneNumber(partnerEntity.phoneNumber())
                .setWebsite(partnerEntity.website())
                .setReference(partnerEntity.reference())
                .setCUI(partnerEntity.CUI())
                .setAddress(partnerEntity.address())
                .setCity(partnerEntity.city())
                .setCounty(partnerEntity.county())
                .setCountry(partnerEntity.country())
                .setPostalCode(partnerEntity.postalCode())
                .setImage(partnerEntity.image() != null ? ImageUtils.encodeImage(partnerEntity.image()) : null)
                .setSaved(partnerEntity.saved());
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

package com.mmdevelopement.crm.domain.user.entity.dto;

import com.mmdevelopement.crm.domain.user.entity.UserEntity;
import com.mmdevelopement.crm.domain.user.entity.UserProfileEntity;
import com.mmdevelopement.crm.domain.user.entity.enums.UserRoles;
import com.mmdevelopement.crm.utils.ImageUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class UserDto {
    private String userGuid;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<UserRoles> roles;
    private String image;


    public static UserDto fromUserEntity(UserEntity userEntity, UserProfileEntity userProfileEntity) {
        return new UserDto()
                .setUserGuid(userEntity.userGuid())
                .setEmail(userEntity.email())
                .setRoles(extractRoles(userEntity))
                .setFirstName(userProfileEntity.firstName())
                .setLastName(userProfileEntity.lastName())
                .setPhoneNumber(userProfileEntity.phoneNumber())
                .setImage(userProfileEntity.image() != null ? ImageUtils.encodeImage(userProfileEntity.image()) : null);
    }

    public static Set<UserRoles> extractRoles(UserEntity userEntity) {
        return Arrays.stream(userEntity.roles()
                        .split(","))
                .map(UserRoles::getRoleFromRoleString)
                .collect(Collectors.toSet());
    }

    public static String formatRoles(Set<UserRoles> roles) {
        return roles.stream()
                .map(UserRoles::getRoleString)
                .collect(Collectors.joining(","));
    }
}

package com.mmdevelopement.crm.domain.user.service;


import com.mmdevelopement.crm.config.security.context.RequestContextHolder;
import com.mmdevelopement.crm.domain.user.entity.UserEntity;
import com.mmdevelopement.crm.domain.user.entity.UserProfileEntity;
import com.mmdevelopement.crm.domain.user.entity.dto.UserDto;
import com.mmdevelopement.crm.domain.user.repository.UserProfileRepository;
import com.mmdevelopement.crm.domain.user.repository.UserRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.HttpStatusException;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import com.mmdevelopement.crm.infrastructure.keycloak.KeycloakAdminService;
import com.mmdevelopement.crm.utils.ImageUtils;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;
import static com.mmdevelopement.crm.domain.user.entity.dto.UserDto.formatRoles;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    private final KeycloakAdminService keycloakService;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> {
                    UserProfileEntity userProfileEntity = userProfileRepository.findByUserId(userEntity.id());

                    return UserDto.fromUserEntity(userEntity, userProfileEntity)
                            .setImage(userProfileEntity.image() != null ? "present, but omitted": null);
                })
                .toList();
    }

    public UserDto getCurrentUser() {
        String userGuid = RequestContextHolder.getCurrentUserGuid();

        UserEntity userEntity = userRepository.findByUserGuid(userGuid);
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserId(userEntity.id());

        if (userEntity == null || userProfileEntity == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return UserDto.fromUserEntity(userEntity, userProfileEntity);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {

        try (Response res = keycloakService.createUser(userDto)) {
            if (res.getStatus() != 201) {
                throw new HttpStatusException("Failed to create user");
            }
        }

        UserEntity userEntity = userRepository.save(new UserEntity()
                .userGuid(keycloakService.getUserByEmail(userDto.getEmail()).getId())
                .email(userDto.getEmail())
                .roles(formatRoles(userDto.getRoles()))
                .status("ACTIVE"));

        UserProfileEntity userProfile = userProfileRepository.save(new UserProfileEntity()
                .userId(userEntity.id())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .image(userDto.getImage() != null ? ImageUtils.decodeImage(userDto.getImage()) : null));

        return UserDto.fromUserEntity(userEntity, userProfile);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        String userGuid = RequestContextHolder.getCurrentUserGuid();

        UserEntity userEntity = userRepository.findByUserGuid(userGuid);
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserId(userEntity.id());

        if (userEntity == null || userProfileEntity == null) {
            throw new ResourceNotFoundException("User not found");
        }

        keycloakService.updateUser(userEntity.userGuid(), userDto);

        userEntity.email(userDto.getEmail())
                .roles(formatRoles(userDto.getRoles()));

        userProfileEntity.firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .image(userDto.getImage() != null ? ImageUtils.decodeImage(userDto.getImage()) : null);

        return UserDto.fromUserEntity(userEntity, userProfileEntity);
    }

    public void updateUserPassword(String password) {
        String userGuid = RequestContextHolder.getCurrentUserGuid();

        keycloakService.updateUserPassword(userGuid, password);
    }
}
